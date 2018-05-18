package com.ym.materials.thrift.client;

import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.swift.service.ThriftClientManager;
import com.ym.materials.thrift.server.HelloService;
import com.ym.materials.thrift.server.User;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

/**
 * Created by ym on 2018/5/17.
 */
public class ThriftClientMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThriftClientManager thriftClientManager = new ThriftClientManager();
        FramedClientConnector connector = new FramedClientConnector(new InetSocketAddress("localhost", 8899));

        User user = new User();
        user.setName("name");
        user.setEmail("email");

        HelloService helloService = thriftClientManager.createClient(connector, HelloService.class).get();
        helloService.sayHello(user);
    }
}
