package com.ym.materials.customProxyFactoryBean.cluster;

import com.ym.materials.customProxyFactoryBean.provider.ProviderServcie;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by ym on 2018/5/27.
 */
public interface ClusterStrategy {

    ProviderServcie select(List<ProviderServcie> providerServcieList);

    static enum ClusterStrategyEnum {
        RANDOM,
        WEIGHT_RANDOM,
        ROLLING,
        WEIGHT_ROLLING,
        HASH
        ;

        ClusterStrategyEnum() {
        }

        public static ClusterStrategyEnum getByName(String clusterStrategyName) {
            for (ClusterStrategyEnum clusterStrategyEnum : ClusterStrategyEnum.values()) {
                if (StringUtils.equals(clusterStrategyName, clusterStrategyEnum.name())) {
                    return clusterStrategyEnum;
                }
            }
            return null;
        }
    }
}
