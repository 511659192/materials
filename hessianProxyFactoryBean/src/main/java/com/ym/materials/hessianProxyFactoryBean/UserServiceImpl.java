package com.ym.materials.hessianProxyFactoryBean;

/**
 * Created by ym on 2018/5/20.
 */
public class UserServiceImpl implements UserService {
    @Override
    public User findByName(String name) {
        return new User(name, "email of " + name);
    }
}
