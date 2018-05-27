package com.ym.materials.customProxyFactoryBean.entity;

/**
 * Created by ym on 2018/5/25.
 */
public class AresResponse {
    private String uniqueKey;
    private Object result;
    private int invokerTimeout;

    public void setInvokeTimeout(int consumeTimeout) {
        this.invokerTimeout = consumeTimeout;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public long getInvokerTimeout() {
        return invokerTimeout;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }
}
