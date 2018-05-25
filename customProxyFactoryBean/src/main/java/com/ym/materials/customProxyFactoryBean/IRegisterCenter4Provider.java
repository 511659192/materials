package com.ym.materials.customProxyFactoryBean;

import java.util.List;
import java.util.Map;

/**
 * Created by ym on 2018/5/24.
 */
public interface IRegisterCenter4Provider {
    Map<String, List<ProviderServcie>> getProviderServiceMap();

    void registerPorvider(List<ProviderServcie> serviceMataData);
}
