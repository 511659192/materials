package com.ym.materials.customProxyFactoryBean.netty;

import com.ym.materials.customProxyFactoryBean.invoker.InvokerResponseHolder;
import com.ym.materials.customProxyFactoryBean.entity.AresResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by ym on 2018/5/27.
 */
public class NettyClientInvokerHandler extends SimpleChannelInboundHandler<AresResponse> {

    public NettyClientInvokerHandler() {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, AresResponse msg) throws Exception {
        InvokerResponseHolder.putResultValue(msg);
    }
}
