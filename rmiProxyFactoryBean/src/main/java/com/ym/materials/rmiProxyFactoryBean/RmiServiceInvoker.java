package com.ym.materials.rmiProxyFactoryBean;

import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by ym on 2018/5/20.
 */
public class RmiServiceInvoker {

    public static void main(String[] args) throws InterruptedException {
        new ClassPathXmlApplicationContext("rmi-rpc-service.xml");
        TimeUnit.SECONDS.sleep(60);
    }
}
