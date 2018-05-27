package com.ym.materials.customProxyFactoryBean.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ym on 2018/5/27.
 */
public class MainServer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("custom-rpc-server.xml");
        System.out.println("server publish is done");
    }
}
