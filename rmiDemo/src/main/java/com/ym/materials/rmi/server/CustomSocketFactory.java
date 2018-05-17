package com.ym.materials.rmi.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

/**
 * Created by ym on 2018/5/17.
 */
public class CustomSocketFactory extends RMISocketFactory {
    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return new Socket(host, port);
    }

    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        port = port == 0 ? 8051 : port;
        System.out.println("current server port:" + port);
        return new ServerSocket(port);
    }
}
