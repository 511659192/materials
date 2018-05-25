package com.ym.materials.hessianProxyFactoryBean;

import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ym on 2018/5/23.
 */
public class HessianInvokeClientMain {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("hessian-rpc-client.xml");
        UserService userService = (UserService) context.getBean("userServiceProxy");
        User user = userService.findByName("name");
        System.out.println(JSON.toJSONString(user));
    }
}
