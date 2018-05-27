package com.ym.materials.customProxyFactoryBean.netty;

import com.ym.materials.customProxyFactoryBean.serializer.SerializeType;
import com.ym.materials.customProxyFactoryBean.serializer.SerializerEngine;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

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
