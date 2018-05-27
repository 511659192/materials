package com.ym.materials.customProxyFactoryBean.netty;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ym.materials.customProxyFactoryBean.util.PropertyConfigeHelper;
import com.ym.materials.customProxyFactoryBean.provider.ProviderServcie;
import com.ym.materials.customProxyFactoryBean.serializer.SerializeType;
import com.ym.materials.customProxyFactoryBean.entity.AresRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.collections4.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ym on 2018/5/25.
 */
public class NettyChannelPoolFactory {

    private final static NettyChannelPoolFactory channelPoolFactory = new NettyChannelPoolFactory();

    private final static Map<InetSocketAddress, ArrayBlockingQueue<Channel>> channelPoolMap = Maps.newConcurrentMap();

    private final static int channelConnectSize = PropertyConfigeHelper.getChannelConnectSize();

    private final static SerializeType serializeType = PropertyConfigeHelper.getSerializeType();

    private List<ProviderServcie> providerServiceList4Invoker = Lists.newArrayList();

    private NettyChannelPoolFactory() {
    }

    public static NettyChannelPoolFactory channelPoolFactoryInstance() {
        return channelPoolFactory;
    }

    public void initChannelPoolFactory(Map<String, List<ProviderServcie>> providerServiceMap4Invoker) {
        Collection<List<ProviderServcie>> providerServiceCollection4Invoker = providerServiceMap4Invoker.values();
        for (List<ProviderServcie> providerServiceList4Invoker : providerServiceCollection4Invoker) {
            if (CollectionUtils.isEmpty(providerServiceList4Invoker)) {
                continue;
            }
            this.providerServiceList4Invoker.addAll(providerServiceList4Invoker);
        }

        Set<InetSocketAddress> socketAddressSet = Sets.newHashSet();
        for (ProviderServcie serviceMetaData : this.providerServiceList4Invoker) {
            String serviceIp = serviceMetaData.getServiceIp();
            Integer servicePort = serviceMetaData.getServicePort();
            InetSocketAddress socketAddress = new InetSocketAddress(serviceIp, servicePort);
            socketAddressSet.add(socketAddress);
        }

        for (InetSocketAddress socketAddress : socketAddressSet) {
            try {
                int realChannelConnectSize = 0;
                while (realChannelConnectSize < channelConnectSize) {
                    Channel channel = null;
                    while (channel == null) {
                        channel = registerChannel(socketAddress);
                    }

                    realChannelConnectSize++;

                    ArrayBlockingQueue<Channel> queue = channelPoolMap.get(socketAddress);
                    if (queue == null) {
                        queue = new ArrayBlockingQueue<Channel>(channelConnectSize);
                        channelPoolMap.put(socketAddress, queue);
                    }
                    queue.offer(channel);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ArrayBlockingQueue<Channel> acquire(InetSocketAddress socketAddress) {
        return channelPoolMap.get(socketAddress);
    }

    public void release(ArrayBlockingQueue<Channel> queue, Channel channel, InetSocketAddress socketAddress) {
        if (CollectionUtils.isEmpty(queue)) {
            return;
        }

        if (channel == null || !channel.isActive() || !channel.isOpen() || !channel.isWritable()) {
            if (channel != null) {
//                channel.deregister().syncUninterruptibly().awaitUninterruptibly();
                channel.closeFuture().syncUninterruptibly().awaitUninterruptibly();
            }

            Channel newChannel = null;
            while (newChannel == null) {
                newChannel = registerChannel(socketAddress);
            }

            queue.offer(newChannel);
            return;
        }

        queue.offer(channel);
    }

    public Channel registerChannel(InetSocketAddress socketAddress) {
        try {
            EventLoopGroup group = new NioEventLoopGroup(10);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.remoteAddress(socketAddress);
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyEncoderHander(serializeType));
                            ch.pipeline().addLast(new NettyDecoderHandler(AresRequest.class, serializeType));
                            ch.pipeline().addLast(new NettyClientInvokerHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect().sync();
            Channel newChannel = channelFuture.channel();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            List<Boolean> isSuccsessHolder = Lists.newArrayListWithCapacity(1);
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        isSuccsessHolder.add(Boolean.TRUE);
                    } else {
                        future.cause().printStackTrace();
                        isSuccsessHolder.add(Boolean.FALSE);
                    }
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await();
            if (isSuccsessHolder.get(0)) {
                return newChannel;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
