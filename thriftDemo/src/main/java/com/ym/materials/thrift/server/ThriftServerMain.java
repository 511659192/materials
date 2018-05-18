package com.ym.materials.thrift.server;

import com.facebook.nifty.core.NettyServerConfig;
import com.facebook.nifty.core.ThriftServerDef;
import com.facebook.nifty.core.ThriftServerDefBuilder;
import com.facebook.swift.codec.ThriftCodecManager;
import com.facebook.swift.service.ThriftEventHandler;
import com.facebook.swift.service.ThriftServer;
import com.facebook.swift.service.ThriftServiceProcessor;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ym on 2018/5/17.
 */
public class ThriftServerMain {

    public static void main(String[] args) {
        ThriftServiceProcessor processor = new ThriftServiceProcessor(
                new ThriftCodecManager(),
                new ArrayList<ThriftEventHandler>(),
                new HelloServiceImpl()
        );

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        ThriftServerDef serverDef = ThriftServerDef.newBuilder()
                .listen(8899)
                .withProcessor(processor)
                .using(executorService)
                .build();
        ExecutorService bossExecutor = Executors.newCachedThreadPool();
        ExecutorService workExecutor = Executors.newCachedThreadPool();

        NettyServerConfig nettyServerConfig = NettyServerConfig.newBuilder()
                .setBossThreadExecutor(bossExecutor)
                .setWorkerThreadExecutor(workExecutor)
                .build();

        ThriftServer server = new ThriftServer(nettyServerConfig, serverDef);
        server.start();
    }
}
