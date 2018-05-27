package com.ym.materials.customProxyFactoryBean.cluster;

import com.ym.materials.customProxyFactoryBean.provider.ProviderServcie;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ym on 2018/5/27.
 */
public class HashClusterStrategyImpl implements ClusterStrategy {

    @Override
    public ProviderServcie select(List<ProviderServcie> providerServcieList) {

        String localIp = IpHelper.getLocalIp();
        int hashCode = localIp.hashCode();
        int size = providerServcieList.size();
        return providerServcieList.get(hashCode % size);
    }
}
