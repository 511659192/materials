package com.ym.materials.rmiProxyFactoryBean;

import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ym on 2018/5/20.
 */
public class RmiClientInvoker {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("rmi-rpc-client.xml");
        UserService userService = (UserService) context.getBean("userServiceProxy");
        User user = userService.findByName("user");
        System.out.println(JSON.toJSONString(user));
    }
}
