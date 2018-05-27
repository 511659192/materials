package com.ym.materials.customProxyFactoryBean.cluster;

import com.ym.materials.customProxyFactoryBean.provider.ProviderServcie;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ym on 2018/5/27.
 */
public class RollingClusterStrategyImpl implements ClusterStrategy {

    private int index;

    private Lock lock = new ReentrantLock();

    @Override
    public ProviderServcie select(List<ProviderServcie> providerServcieList) {
        ProviderServcie providerServcie = null;
        try {
            lock.tryLock(10, TimeUnit.MICROSECONDS);
            if (index >= providerServcieList.size()) {
                index = 0;
            }
            providerServcie = providerServcieList.get(index++);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        if (providerServcie == null) {
            providerServcie = providerServcieList.get(0);
        }
        return providerServcie;
    }
}
