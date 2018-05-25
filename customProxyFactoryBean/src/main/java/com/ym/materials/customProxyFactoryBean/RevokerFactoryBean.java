package com.ym.materials.customProxyFactoryBean;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;

/**
 * Created by ym on 2018/5/25.
 */
public class RevokerFactoryBean implements FactoryBean, InitializingBean {

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
        registerCenter4Invoker.initProviderMap(remoteAppKey, groupName);
        Map<String, List<ProviderServcie>> providerMap = registerCenter4Invoker.getServiceMetaDataMap4Invoker();
        if (MapUtils.isEmpty(providerMap)) {
            throw new RuntimeException("no provider is available");
        }

        NettyChannelPoolFactory.channelPoolFactoryInstance().initChannelPoolFactory(providerMap);
        RevokerProxyBeanFactory proxyBeanFactory = RevokerProxyBeanFactory.singleton(targetInterface, timeout, clusterStrategy);
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
