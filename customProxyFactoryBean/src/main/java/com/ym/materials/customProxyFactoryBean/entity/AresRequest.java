package com.ym.materials.customProxyFactoryBean.entity;

import com.ym.materials.customProxyFactoryBean.provider.ProviderServcie;

/**
 * Created by ym on 2018/5/24.
 */
public class AresRequest {
    private String uniqueKey;
    private ProviderServcie providerService;
    private int invokeTimeout;
    private String invokeMethodName;
    private Object[] args;

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public ProviderServcie getProviderService() {
        return providerService;
    }

    public void setProviderService(ProviderServcie providerService) {
        this.providerService = providerService;
    }

    public int getInvokeTimeout() {
        return invokeTimeout;
    }

    public void setInvokeTimeout(int invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }

    public String getInvokeMethodName() {
        return invokeMethodName;
    }

    public void setInvokeMethodName(String invokeMethodName) {
        this.invokeMethodName = invokeMethodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
