package com.ym.materials.customProxyFactoryBean.cluster;

import com.google.common.collect.Lists;
import com.ym.materials.customProxyFactoryBean.provider.ProviderServcie;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ym on 2018/5/27.
 */
public class WeightRollingClusterStrategyImpl implements ClusterStrategy {

    private int index;

    private Lock lock = new ReentrantLock();

    @Override
    public ProviderServcie select(List<ProviderServcie> providerServcieList) {
        ProviderServcie providerServcie = null;
        try {
            lock.tryLock(10, TimeUnit.MICROSECONDS);

            List<ProviderServcie> weightedProvicerServiceList = Lists.newArrayList();
            for (ProviderServcie servcie : providerServcieList) {
                int weight = servcie.getWeight();
                for (int i = 0; i < weight; i++) {
                    weightedProvicerServiceList.add(servcie.copy());
                }
            }

            if (index >= weightedProvicerServiceList.size()) {
                index = 0;
            }
            providerServcie = weightedProvicerServiceList.get(index++);
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
