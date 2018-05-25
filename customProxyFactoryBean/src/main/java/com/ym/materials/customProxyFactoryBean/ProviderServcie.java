package com.ym.materials.customProxyFactoryBean;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Created by ym on 2018/5/24.
 */
public class ProviderServcie {

    private Class<?> servicerItf;
    private Object serviceObject;
    private String serviceIp;
    private Integer servicePort;
    private Long timeout;
    private Method serviceMethod;
    private int weight;
    private int workerThreads;
    private String appKey;
    private String groupName;

    public Class<?> getServicerItf() {
        return servicerItf;
    }

    public ProviderServcie setServicerItf(Class<?> servicerItf) {
        this.servicerItf = servicerItf;
        return this;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public ProviderServcie setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
        return this;
    }

    public String getServiceIp() {
        return serviceIp;
    }

    public ProviderServcie setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
        return this;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public ProviderServcie setServicePort(Integer servicePort) {
        this.servicePort = servicePort;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public ProviderServcie setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public Method getServiceMethod() {
        return serviceMethod;
    }

    public ProviderServcie setServiceMethod(Method serviceMethod) {
        this.serviceMethod = serviceMethod;
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public ProviderServcie setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public ProviderServcie setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
        return this;
    }

    public String getAppKey() {
        return appKey;
    }

    public ProviderServcie setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public ProviderServcie setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }
}
