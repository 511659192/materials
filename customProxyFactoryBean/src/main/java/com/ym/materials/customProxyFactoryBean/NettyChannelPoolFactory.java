package com.ym.materials.customProxyFactoryBean;

import java.util.List;
import java.util.Map;

/**
 * Created by ym on 2018/5/25.
 */
public class NettyChannelPoolFactory {
    public static NettyChannelPoolFactory channelPoolFactoryInstance() {
        return new NettyChannelPoolFactory();
    }

    public void initChannelPoolFactory(Map<String, List<ProviderServcie>> providerMap) {

    }
}
