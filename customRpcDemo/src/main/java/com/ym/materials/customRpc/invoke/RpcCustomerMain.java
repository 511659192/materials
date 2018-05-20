package com.ym.materials.customRpc.invoke;

import com.ym.materials.customRpc.framework.CustomerProxy;
import com.ym.materials.customRpc.service.HelloService;

/**
 * Created by ym on 2018/5/19.
 */
public class RpcCustomerMain {

    public static void main(String[] args) {
        HelloService helloService = CustomerProxy.consume(HelloService.class, "localhost", 8083);
        for (int i = 0; i < 10; i++) {
            String result = helloService.sayHello("current num is " + i);
            System.out.println(result);
        }
    }
}
