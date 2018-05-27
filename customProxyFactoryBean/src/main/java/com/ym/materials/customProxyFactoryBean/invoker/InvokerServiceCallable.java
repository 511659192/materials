package com.ym.materials.customProxyFactoryBean.invoker;

import com.ym.materials.customProxyFactoryBean.entity.AresRequest;
import com.ym.materials.customProxyFactoryBean.entity.AresResponse;
import com.ym.materials.customProxyFactoryBean.netty.NettyChannelPoolFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by ym on 2018/5/27.
 */
public class InvokerServiceCallable implements Callable<AresResponse> {

    private Channel channel;
    private InetSocketAddress socketAddress;
    private AresRequest request;

    public InvokerServiceCallable(InetSocketAddress socketAddress, AresRequest request) {
        this.socketAddress = socketAddress;
        this.request = request;
    }

    public static InvokerServiceCallable of(InetSocketAddress socketAddress, AresRequest request) {
        return new InvokerServiceCallable(socketAddress, request);
    }

    @Override
    public AresResponse call() throws Exception {
        InvokerResponseHolder.initResponseData(request.getUniqueKey());
        ArrayBlockingQueue<Channel> queue = NettyChannelPoolFactory.channelPoolFactoryInstance().acquire(socketAddress);
        try {
            if (channel == null) {
                channel = queue.poll(request.getInvokeTimeout(), TimeUnit.MICROSECONDS);
            }

            while (!channel.isOpen() || !channel.isActive() || !channel.isWritable()) {
                channel = queue.poll(request.getInvokeTimeout(), TimeUnit.MICROSECONDS);
                if (channel == null) {
                    channel = NettyChannelPoolFactory.channelPoolFactoryInstance().registerChannel(socketAddress);
                }
            }

            ChannelFuture channelFuture = channel.writeAndFlush(request);
            channelFuture.syncUninterruptibly();
            int invokeTimeout = request.getInvokeTimeout();
            return InvokerResponseHolder.getValue(request.getUniqueKey(), invokeTimeout);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            NettyChannelPoolFactory.channelPoolFactoryInstance().release(queue, channel, socketAddress);
        }
    }
}
