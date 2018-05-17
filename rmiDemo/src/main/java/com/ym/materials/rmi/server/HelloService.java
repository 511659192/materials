package com.ym.materials.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by ym on 2018/5/17.
 */
public interface HelloService extends Remote {

    void syaHello() throws RemoteException;
}
