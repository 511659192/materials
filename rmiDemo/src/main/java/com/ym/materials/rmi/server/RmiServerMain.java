package com.ym.materials.rmi.server;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMISocketFactory;

/**
 * Created by ym on 2018/5/17.
 */
public class RmiServerMain {

    public static void main(String[] args) throws IOException, AlreadyBoundException {
        HelloService helloService = new HelloServiceImpl();
        LocateRegistry.createRegistry(8801);
        RMISocketFactory.setSocketFactory(new CustomSocketFactory());
        Naming.bind("rmi://localhost:8801/helloService", helloService);
        System.out.println("service has bean provided");
    }
}
