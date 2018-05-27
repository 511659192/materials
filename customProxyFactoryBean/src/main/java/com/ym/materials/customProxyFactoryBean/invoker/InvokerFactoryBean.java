package com.ym.materials.customProxyFactoryBean.invoker;

import com.ym.materials.customProxyFactoryBean.netty.NettyChannelPoolFactory;
import com.ym.materials.customProxyFactoryBean.provider.ProviderServcie;
import com.ym.materials.customProxyFactoryBean.register.IRegisterCenter4Invoker;
import com.ym.materials.customProxyFactoryBean.register.RegisterCenter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;

/**
 * Created by ym on 2018/5/25.
 */
public class InvokerFactoryBean implements FactoryBean, InitializingBean {

    private Class<?> targetInterface;
    private int timeout;
    private Object serviceObject;
    private String clusterStrategy;
    private String remoteAppKey;
    private String groupName;

    @Override
    public Object getObject() throws Exception {
        return serviceObject;
    }

    @Override
    public Class<?> getObjectType() {
        return targetInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        IRegisterCenter4Invoker registerCenter4Invoker = RegisterCenter.singleton();
        registerCenter4Invoker.initProviderServiceMap4Invoker(remoteAppKey, groupName);
        Map<String, List<ProviderServcie>> providerServiceMap4Invoker = registerCenter4Invoker.getProviderServiceMap4Invoker();
        if (MapUtils.isEmpty(providerServiceMap4Invoker)) {
            throw new RuntimeException("no provider is available");
        }

        NettyChannelPoolFactory nettyChannelPoolFactory = NettyChannelPoolFactory.channelPoolFactoryInstance();
        nettyChannelPoolFactory.initChannelPoolFactory(providerServiceMap4Invoker);
        InvokerProxyBeanFactory proxyBeanFactory = InvokerProxyBeanFactory.singleton(targetInterface, timeout, clusterStrategy);
        this.serviceObject = proxyBeanFactory.getProxy();

        InvokerService invoker = new InvokerService();
        invoker.setServiceItf(targetInterface);
        invoker.setRemoteAppKey(remoteAppKey);
        invoker.setGroupName(groupName);
        registerCenter4Invoker.registerInvoker(invoker);
    }

    public Class<?> getTargetInterface() {
        return targetInterface;
    }

    public void setTargetInterface(Class<?> targetInterface) {
        this.targetInterface = targetInterface;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public void setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
    }

    public String getClusterStrategy() {
        return clusterStrategy;
    }

    public void setClusterStrategy(String clusterStrategy) {
        this.clusterStrategy = clusterStrategy;
    }

    public String getRemoteAppKey() {
        return remoteAppKey;
    }

    public void setRemoteAppKey(String remoteAppKey) {
        this.remoteAppKey = remoteAppKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
