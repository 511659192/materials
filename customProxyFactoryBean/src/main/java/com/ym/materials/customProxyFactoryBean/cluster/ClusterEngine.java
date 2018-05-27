package com.ym.materials.customProxyFactoryBean.cluster;

import com.google.common.collect.Maps;
import com.ym.materials.customProxyFactoryBean.cluster.ClusterStrategy.ClusterStrategyEnum;

import java.util.Map;

/**
 * Created by ym on 2018/5/27.
 */
public class ClusterEngine {

    private final static Map<ClusterStrategyEnum, ClusterStrategy> clusterStrategyMap = Maps.newHashMap();

    static {
        clusterStrategyMap.put(ClusterStrategyEnum.RANDOM, new RandomClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.WEIGHT_RANDOM, new WeightRandomClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.ROLLING, new RollingClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.WEIGHT_ROLLING, new WeightRollingClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.HASH, new HashClusterStrategyImpl());
    }

    public static ClusterStrategy getClusterStategy(String clusterStategy) {
        ClusterStrategyEnum clusterStrategyEnum = ClusterStrategyEnum.getByName(clusterStategy);
        if (clusterStrategyEnum != null) {
            return clusterStrategyMap.get(clusterStrategyEnum);
        }
        return new RandomClusterStrategyImpl();
    }
}
