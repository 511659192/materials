package com.ym.materials.customProxyFactoryBean;

/**
 * Created by ym on 2018/5/25.
 */
public class AresResponse {
    private Object uniqueKey;
    private Object result;

    public void setInvokeTimeout(Long consumeTimeout) {

    }

    public void setUniqueKey(Object uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
