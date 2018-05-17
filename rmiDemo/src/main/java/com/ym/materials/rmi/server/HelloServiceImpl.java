package com.ym.materials.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 服务端必须实现UnicastRemoteObject
 * Created by ym on 2018/5/17.
 */
public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {

    public HelloServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void syaHello() throws RemoteException {
        System.out.println("sayHello");
    }
}
