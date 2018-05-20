package com.ym.materials.customRpc.framework;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ym on 2018/5/19.
 */
public class ProviderReflect {

    private final static ExecutorService executors = Executors.newCachedThreadPool();

    public static void provider(final Object serivce ,int port) throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            final Socket socket = serverSocket.accept();
            executors.submit(new Runnable() {
                @Override
                public void run() {
                    ObjectInputStream objectInputStream = null;
                    ObjectOutputStream objectOutputStream = null;
                    try {
                        objectInputStream = new ObjectInputStream(socket.getInputStream());
                        String method = objectInputStream.readUTF();
                        Object[] params = (Object[]) objectInputStream.readObject();
                        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        Object result = MethodUtils.invokeExactMethod(serivce, method, params);
                        objectOutputStream.writeObject(result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        try {
                            objectInputStream.close();
                            objectOutputStream.close();
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }


    }

}
