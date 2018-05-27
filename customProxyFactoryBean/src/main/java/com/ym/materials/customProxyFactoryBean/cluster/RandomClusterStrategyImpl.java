package com.ym.materials.customProxyFactoryBean.cluster;

import com.ym.materials.customProxyFactoryBean.provider.ProviderServcie;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * Created by ym on 2018/5/27.
 */
public class RandomClusterStrategyImpl implements ClusterStrategy {
    @Override
    public ProviderServcie select(List<ProviderServcie> providerServcieList) {
        int maxSize = providerServcieList.size();
        int index = RandomUtils.nextInt(0, maxSize - 1);
        return providerServcieList.get(index);
    }
}
