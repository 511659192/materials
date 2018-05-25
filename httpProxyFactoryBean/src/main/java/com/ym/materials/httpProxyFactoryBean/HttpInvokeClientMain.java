package com.ym.materials.httpProxyFactoryBean;

import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ym on 2018/5/23.
 */
public class HttpInvokeClientMain {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("http-rpc-client.xml");
        UserService userService = (UserService) context.getBean("userServiceProxy");
        User user = userService.findByName("name");
        System.out.println(JSON.toJSONString(user));
    }
}
