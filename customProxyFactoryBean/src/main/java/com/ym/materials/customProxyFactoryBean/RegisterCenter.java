package com.ym.materials.customProxyFactoryBean;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by ym on 2018/5/24.
 */
public class RegisterCenter implements IRegisterCenter4Provider, IRegisterCenter4Invoker {

    private static RegisterCenter registerCenter = new RegisterCenter();

    private final static Map<String, List<ProviderServcie>> providerServiceMap = Maps.newConcurrentMap();

    private final static Map<String, List<ProviderServcie>> serviceMataDataMap4Invoker = Maps.newConcurrentMap();

    private static String ZK_SERVER = PropertyConfigeHelper.getZKService();
    private static int ZK_SESSION_TIMEOUT = PropertyConfigeHelper.getZKSessionTimeout();
    private static int ZK_CONNECTION_TIMEOUT = PropertyConfigeHelper.getZKConnectionTimeout();
    private static String ROOT_PATH = "/config_register/" + PropertyConfigeHelper.getAppName();
    private static String PROVIDER_TYPE = "/provider";
    private static String INVOKER_TYPE = "/invoker";
    private static volatile ZkClient zkClient;

    private RegisterCenter() {
    }

    public static RegisterCenter singleton() {
        return registerCenter;
    }

    @Override
    public Map<String, List<ProviderServcie>> getProviderServiceMap() {
        return providerServiceMap;
    }

    @Override
    public void registerPorvider(List<ProviderServcie> serviceMataDataList) {
        if (CollectionUtils.isEmpty(serviceMataDataList)) {
            return;
        }

        synchronized (RegisterCenter.class) {
            for (ProviderServcie provider : serviceMataDataList) {
                String serviceItfKey = provider.getServicerItf().getName();
                List<ProviderServcie> providerServcieList = providerServiceMap.get(serviceItfKey);
                if (providerServcieList == null) {
                    providerServcieList = Lists.newArrayList();
                    providerServiceMap.put(serviceItfKey, providerServcieList);
                }
                providerServcieList.add(provider);
            }

            if (zkClient == null) {
                zkClient = new ZkClient(ZK_SERVER, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT, new SerializableSerializer());
                if (!zkClient.exists(ROOT_PATH)) {
                    zkClient.createPersistent(ROOT_PATH, true);
                }
                for (Map.Entry<String, List<ProviderServcie>> entry : providerServiceMap.entrySet()) {
                    String serviceNode = entry.getKey();
                    String servicePath = ROOT_PATH + "/" + serviceNode + PROVIDER_TYPE;
                    if (!zkClient.exists(servicePath)) {
                        zkClient.createPersistent(servicePath, true);
                    }

                    List<ProviderServcie> providerServcieList = entry.getValue();
                    ProviderServcie master = providerServcieList.get(0);
                    Integer servicePort = master.getServicePort();
                    // TODO: 2018/5/25
                    String localIP = "ip";
                    String serviceIpNode = servicePath + "/" + localIP + "|" + servicePort;
                    if (!zkClient.exists(serviceIpNode)) {
                        zkClient.createEphemeral(serviceIpNode);
                    }

                    zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
                        @Override
                        public void handleChildChange(String s, List<String> currentChilds) throws Exception {
                            if (CollectionUtils.isEmpty(currentChilds)) {
                                return;
                            }

                            List<String> activityServiceIpList = Lists.transform(currentChilds, new Function<String, String>() {
                                @Override
                                public String apply(String input) {
                                    return Splitter.on("|").splitToList(input).get(0);
                                }
                            });

                            refreshActivityService(activityServiceIpList);
                        }
                    });
                }
            }
        }
    }

    private void refreshActivityService(List<String> activityServiceIpList) {
    }

    @Override
    public void initProviderMap(String remoteAppKey, String groupName) {
        if (MapUtils.isEmpty(serviceMataDataMap4Invoker)) {
            serviceMataDataMap4Invoker.putAll(fetchOrUpdateProviderServiceMetaData());
        }
    }

    private Map<? extends String, ? extends List<ProviderServcie>> fetchOrUpdateProviderServiceMetaData() {
        return null;
    }

    @Override
    public void initProviderMap() {
        if (MapUtils.isEmpty(serviceMataDataMap4Invoker)) {
            serviceMataDataMap4Invoker.putAll(fetchOrUpdateProviderServiceMetaData());
        }
    }

    @Override
    public Map<String, List<ProviderServcie>> getServiceMetaDataMap4Invoker() {
        return serviceMataDataMap4Invoker;
    }

    @Override
    public void registerInvoker(InvokerService invoker) {
        if (invoker == null) {
            return;
        }

        if (zkClient == null) {
            zkClient = new ZkClient(ZK_SERVER, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT, new SerializableSerializer());
        }

        if (!zkClient.exists(ROOT_PATH)) {
            zkClient.createPersistent(ROOT_PATH, true);
        }
    }
}
