package com.ym.materials.customRpc.service;

/**
 * Created by ym on 2018/5/19.
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String content) {
        String text = "hello" + content;
        System.out.println(text);
        return "hello " + content;
    }
}
