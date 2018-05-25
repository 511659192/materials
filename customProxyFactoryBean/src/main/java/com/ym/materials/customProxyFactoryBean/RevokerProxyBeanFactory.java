package com.ym.materials.customProxyFactoryBean;

/**
 * Created by ym on 2018/5/25.
 */
public class RevokerProxyBeanFactory {
    private Object proxy;

    public static RevokerProxyBeanFactory singleton(Class<?> targetInterface, int timeout, String clusterStrategy) {
        return null;
    }

    public Object getProxy() {
        return proxy;
    }
}
