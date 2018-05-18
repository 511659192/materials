package com.ym.materials.thrift.server;

/**
 * Created by ym on 2018/5/17.
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello(User user) {
        System.out.println("hello " + user.getName() + " " + user.getEmail());
    }
}
