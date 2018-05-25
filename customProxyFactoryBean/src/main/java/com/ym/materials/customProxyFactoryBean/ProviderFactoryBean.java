package com.ym.materials.customProxyFactoryBean;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by ym on 2018/5/23.
 */
public class ProviderFactoryBean implements FactoryBean, InitializingBean {

    private Class<?> serviceItf;

    private Object serviceObject;

    private String servicePort;

    private Long timeout;

    private Object serviceProxyObject;

    private String appKey;

    private String groupName = "default";

    private int weight;
    private int workerThreads;

    @Override
    public Object getObject() throws Exception {
        return serviceProxyObject;
    }

    @Override
    public Class<?> getObjectType() {
        return serviceItf;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NettyServer.singleton().start(Integer.valueOf(servicePort));
        List<ProviderServcie> providerServcieList = buildProviderServiceInfos();
        IRegisterCenter4Provider registerCenter4Provider = RegisterCenter.singleton();
        registerCenter4Provider.registerPorvider(providerServcieList);
    }

    private List<ProviderServcie> buildProviderServiceInfos() {
        List<ProviderServcie> providerServcieList = Lists.newArrayList();
        Method[] declaredMethods = serviceObject.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            ProviderServcie providerServcie = new ProviderServcie();
            providerServcie.setServicerItf(serviceItf);
            providerServcie.setServiceObject(serviceObject);
            providerServcie.setServiceIp("localhost");
            providerServcie.setServicePort(Integer.valueOf(servicePort));
            providerServcie.setTimeout(timeout);
            providerServcie.setServiceMethod(method);
            providerServcie.setWeight(weight);
            providerServcie.setWorkerThreads(workerThreads);
            providerServcie.setAppKey(appKey);
            providerServcie.setGroupName(groupName);
            providerServcieList.add(providerServcie);
        }
        return providerServcieList;
    }

    public Class<?> getServiceItf() {
        return serviceItf;
    }

    public void setServiceItf(Class<?> serviceItf) {
        this.serviceItf = serviceItf;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public void setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
    }

    public String getServicePort() {
        return servicePort;
    }

    public void setServicePort(String servicePort) {
        this.servicePort = servicePort;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Object getServiceProxyObject() {
        return serviceProxyObject;
    }

    public void setServiceProxyObject(Object serviceProxyObject) {
        this.serviceProxyObject = serviceProxyObject;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }
}
