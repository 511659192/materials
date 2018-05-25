package com.ym.materials.customProxyFactoryBean;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Created by ym on 2018/5/24.
 */
public class NettyEncoderHander extends MessageToByteEncoder {

    private SerializeType serializeType;

    public NettyEncoderHander(SerializeType serializeType) {
        this.serializeType = serializeType;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] bytes = SerializerEngine.serialize(msg, serializeType.getSerializeType());
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
