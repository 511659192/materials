package com.ym.materials.customProxyFactoryBean;

/**
 * Created by ym on 2018/5/25.
 */
public class InvokerService {
    private Class<?> serviceItf;
    private String remoteAppKey;
    private String groupName;

    public void setServiceItf(Class<?> serviceItf) {
        this.serviceItf = serviceItf;
    }

    public void setRemoteAppKey(String remoteAppKey) {
        this.remoteAppKey = remoteAppKey;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
