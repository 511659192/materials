package com.ym.materials.customProxyFactoryBean.register;

import com.ym.materials.customProxyFactoryBean.invoker.InvokerService;
import com.ym.materials.customProxyFactoryBean.provider.ProviderServcie;

import java.util.List;
import java.util.Map;

/**
 * Created by ym on 2018/5/24.
 */
public interface IRegisterCenter4Invoker {
    void initProviderServiceMap4Invoker(String remoteAppKey, String groupName);

    void initProviderServiceMap4Invoker();

    Map<String,List<ProviderServcie>> getProviderServiceMap4Invoker();

    void registerInvoker(InvokerService invoker);
}
