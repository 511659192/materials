package com.ym.materials.customProxyFactoryBean.netty;

import com.ym.materials.customProxyFactoryBean.util.PropertyConfigeHelper;
import com.ym.materials.customProxyFactoryBean.serializer.SerializeType;
import com.ym.materials.customProxyFactoryBean.entity.AresRequest;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by ym on 2018/5/24.
 */
public class NettyServer {

    private static NettyServer nettyServer = new NettyServer();

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private SerializeType serializeType = PropertyConfigeHelper.getSerializeType();

    public void start(final int port) {
        synchronized (NettyServer.class) {
            if (bossGroup != null || workerGroup != null) {
                return;
            }

            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new NettyDecoderHandler(AresRequest.class, serializeType));
                            channel.pipeline().addLast(new NettyEncoderHander(serializeType));
                            channel.pipeline().addLast(new NettyServerInvokeHandler());
                        }
                    });

            try {
                serverBootstrap.bind(port).sync().channel();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public NettyServer() {
    }

    public static NettyServer singleton() {
        return nettyServer;
    }
}
