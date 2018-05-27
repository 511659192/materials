package com.ym.materials.customProxyFactoryBean.provider;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;

/**
 * Created by ym on 2018/5/24.
 */
public class ProviderServcie {

    private Class<?> serviceItf;
    private Object serviceObject;
    private String serviceIp;
    private Integer servicePort;
    private Long timeout;
    private Method serviceMethod;
    private int weight;
    private int workerThreads;
    private String appKey;
    private String groupName;

    public Class<?> getServiceItf() {
        if (serviceItf == null) {
            return serviceObject.getClass().getInterfaces()[0];
        }
        return serviceItf;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public String getServiceIp() {
        return serviceIp;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public Long getTimeout() {
        return timeout;
    }

    public Method getServiceMethod() {
        return serviceMethod;
    }

    public int getWeight() {
        return weight;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setServiceItf(Class<?> serviceItf) {
        this.serviceItf = serviceItf;
    }

    public void setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    public void setServicePort(Integer servicePort) {
        this.servicePort = servicePort;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public void setServiceMethod(Method serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ProviderServcie copy() {
        ProviderServcie copy = new ProviderServcie();
        BeanUtils.copyProperties(this, copy);
        return copy;
    }
}
