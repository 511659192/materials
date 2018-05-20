package com.ym.materials.customRpc.framework;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * Created by ym on 2018/5/19.
 */
public class CustomerProxy {

    public static <T> T consume(final Class<T> interfaceClass, final String host, final int port) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = null;
                        ObjectOutputStream objectOutputStream = null;
                        ObjectInputStream objectInputStream = null;
                        try {
                            socket = new Socket(host, port);
                            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            objectOutputStream.writeUTF(method.getName());
                            objectOutputStream.writeObject(args);
                            objectInputStream = new ObjectInputStream(socket.getInputStream());
                            Object result = objectInputStream.readObject();
                            if (result instanceof Throwable) {
                                throw (Throwable) result;
                            }
                            return result;
                        } finally {
                            if (objectInputStream != null) objectInputStream.close();;
                            if (objectOutputStream != null) objectOutputStream.close();
                            if (socket != null) socket.close();
                        }
                    }
                });
    }
}
