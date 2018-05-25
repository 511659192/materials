package com.ym.materials.customProxyFactoryBean;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.channel.DefaultChannelHandlerInvoker;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.List;

/**
 * Created by ym on 2018/5/24.
 */
public class NettyDecoderHandler extends ByteToMessageDecoder {


    private Class<?> genericClass;

    private SerializeType serializeType;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }

        in.markReaderIndex();
        int len = in.readInt();
        if (len < 0) {
            ctx.close();
        }

        if (in.readableBytes() < len) {
            in.resetReaderIndex();
            return;
        }

        byte[] data = new byte[len];
        in.readBytes(data);

        Object result = SerializerEngine.deserialize(data, genericClass, serializeType.getSerializeType());
        out.add(result);
    }

    public NettyDecoderHandler(Class<?> genericClass, SerializeType serializeType) {
        this.genericClass = genericClass;
        this.serializeType = serializeType;
    }
}
