package com.ym.materials.rmi.client;

import com.ym.materials.rmi.server.HelloService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by ym on 2018/5/17.
 */
public class RmiClientMain {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        HelloService helloService = (HelloService) Naming.lookup("rmi://localhost:8801/helloService");
        helloService.syaHello();
    }
}
