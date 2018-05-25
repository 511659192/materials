package com.ym.materials.customProxyFactoryBean;

import java.util.List;
import java.util.Map;

/**
 * Created by ym on 2018/5/24.
 */
public interface IRegisterCenter4Invoker {
    void initProviderMap(String remoteAppKey, String groupName);

    void initProviderMap();

    Map<String,List<ProviderServcie>> getServiceMetaDataMap4Invoker();

    void registerInvoker(InvokerService invoker);
}
