package com.ym.materials.customRpc.invoke;

import com.ym.materials.customRpc.framework.ProviderReflect;
import com.ym.materials.customRpc.service.HelloService;
import com.ym.materials.customRpc.service.HelloServiceImpl;

/**
 * Created by ym on 2018/5/19.
 */
public class RpcProviderMain {

    public static void main(String[] args) throws Exception {
        HelloService helloService = new HelloServiceImpl();
        ProviderReflect.provider(helloService, 8083);
    }
}
