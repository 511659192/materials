package com.ym.materials.customProxyFactoryBean.service;

/**
 * Created by ym on 2018/5/25.
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        System.out.println("hello " +name);
        return "hello " +name;
    }
}
