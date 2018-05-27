package com.ym.materials.customProxyFactoryBean.cluster;

import com.google.common.collect.Lists;
import com.ym.materials.customProxyFactoryBean.provider.ProviderServcie;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * Created by ym on 2018/5/27.
 */
public class WeightRandomClusterStrategyImpl implements ClusterStrategy {
    @Override
    public ProviderServcie select(List<ProviderServcie> providerServcieList) {
        List<ProviderServcie> weightedProviderServiceList = Lists.newArrayList();
        for (ProviderServcie providerServcie : providerServcieList) {
            int weight = providerServcie.getWeight();
            for (int i = 0; i < weight; i++) {
                weightedProviderServiceList.add(providerServcie.copy());
            }
        }

        int maxSize = weightedProviderServiceList.size();
        int index = RandomUtils.nextInt(0, maxSize - 1);
        return weightedProviderServiceList.get(index);
    }
}
