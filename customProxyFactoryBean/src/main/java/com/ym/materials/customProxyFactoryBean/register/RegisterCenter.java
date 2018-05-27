package com.ym.materials.customProxyFactoryBean.register;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ym.materials.customProxyFactoryBean.util.PropertyConfigeHelper;
import com.ym.materials.customProxyFactoryBean.invoker.InvokerService;
import com.ym.materials.customProxyFactoryBean.provider.ProviderServcie;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ClassUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by ym on 2018/5/24.
 */
public class RegisterCenter implements IRegisterCenter4Provider, IRegisterCenter4Invoker {

    private static RegisterCenter registerCenter = new RegisterCenter();

    private final static Map<String, List<ProviderServcie>> providerServiceMap = Maps.newConcurrentMap();

    private final static Map<String, List<ProviderServcie>> providerServiceMap4Invoker = Maps.newConcurrentMap();

    private static String ZK_SERVER = PropertyConfigeHelper.getZKService();
    private static int ZK_SESSION_TIMEOUT = PropertyConfigeHelper.getZKSessionTimeout();
    private static int ZK_CONNECTION_TIMEOUT = PropertyConfigeHelper.getZKConnectionTimeout();
    private static String ROOT_PATH = "/config_register";
    private static String APP_PATH = ROOT_PATH + "/" + PropertyConfigeHelper.getAppName();
    private static String PROVIDER_TYPE = "/provider";
    private static String INVOKER_TYPE = "/invoker";
    private static volatile ZkClient zkClient;

    private RegisterCenter() {
    }

    public static RegisterCenter singleton() {
        return registerCenter;
    }

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
                String serviceItfKey = provider.getServiceItf().getName();
                List<ProviderServcie> providerServcieList = providerServiceMap.get(serviceItfKey);
                if (providerServcieList == null) {
                    providerServcieList = Lists.newArrayList();
                    providerServiceMap.put(serviceItfKey, providerServcieList);
                }
                providerServcieList.add(provider);
            }

            if (zkClient == null) {
                zkClient = new ZkClient(ZK_SERVER, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT, new SerializableSerializer());
            }

            if (!zkClient.exists(APP_PATH)) {
                zkClient.createPersistent(APP_PATH, true);
            }

            for (Map.Entry<String, List<ProviderServcie>> entry : providerServiceMap.entrySet()) {
                String providerItfKey = entry.getKey();
                String providerPath = APP_PATH + "/" + providerItfKey + PROVIDER_TYPE;
                if (!zkClient.exists(providerPath)) {
                    zkClient.createPersistent(providerPath, true);
                }

                List<ProviderServcie> providerServcieList = entry.getValue();
                ProviderServcie current = providerServcieList.get(0);
                Integer servicePort = current.getServicePort();
                String localIP = "127.0.0.1";
                String ipPath = providerPath + "/" + localIP + "|" + servicePort;
                if (!zkClient.exists(ipPath)) {
                    zkClient.createEphemeral(ipPath);
                }

                zkClient.subscribeChildChanges(providerPath, new IZkChildListener() {
                    @Override
                    public void handleChildChange(String s, List<String> currentChilds) throws Exception {
                        currentChilds = currentChilds == null ? Lists.newArrayList() : currentChilds;

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

    private void refreshActivityService(List<String> ipList) {
        ipList = ipList == null ? Lists.newArrayList() : ipList;

        Map<String, List<ProviderServcie>> currentProviderServiceMap = Maps.newHashMap();

        for (Map.Entry<String, List<ProviderServcie>> entry : providerServiceMap.entrySet()) {
            String serviceItfKey = entry.getKey();
            List<ProviderServcie> currentProviderServiceList = currentProviderServiceMap.get(serviceItfKey);
            if (currentProviderServiceList == null) {
                currentProviderServiceList = Lists.newArrayList();
                currentProviderServiceMap.put(serviceItfKey, currentProviderServiceList);
            }

            List<ProviderServcie> providerServcieList = entry.getValue();
            for (ProviderServcie serviceMetaData : providerServcieList) {
                if (ipList.contains(serviceMetaData.getServiceIp())) { // 如果当前节点有效 添加到临时map 否则providerService将从本地服务列表中删除
                    currentProviderServiceList.add(serviceMetaData);
                }
            }
        }
        providerServiceMap.putAll(currentProviderServiceMap);
    }

    @Override
    public void initProviderServiceMap4Invoker(String remoteAppKey, String groupName) {
        initProviderServiceMap4Invoker();
    }

    private Map<String, List<ProviderServcie>> fetchOrUpdateProviderServiceMap4Invoker() {
        Map<String, List<ProviderServcie>> currentProviderServiceMap4Invoker = Maps.newHashMap();
        synchronized (RegisterCenter.class) {
            if (zkClient == null) {
                zkClient = new ZkClient(ZK_SERVER, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT, new SerializableSerializer());
            }

            String rootPath = ROOT_PATH;
            List<String> serviceItfKeyList = zkClient.getChildren(rootPath);
            for (String serviceItfKey : serviceItfKeyList) {
                String providerPath = rootPath + "/" + serviceItfKey + PROVIDER_TYPE;
                List<String> ipPathList = zkClient.getChildren(providerPath);
                for (String ipPath : ipPathList) {
                    List<String> ipArr = Splitter.on("|").splitToList(ipPath);
                    String ip = ipArr.get(0);
                    String port = ipArr.get(1);
                    List<ProviderServcie> providerServcieList = currentProviderServiceMap4Invoker.get(serviceItfKey);
                    if (CollectionUtils.isEmpty(providerServcieList)) {
                        providerServcieList = Lists.newArrayList();
                        currentProviderServiceMap4Invoker.put(serviceItfKey, providerServcieList);
                    }
                    ProviderServcie providerServcie = new ProviderServcie();
                    try {
                        providerServcie.setServiceItf(ClassUtils.getClass(serviceItfKey));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    providerServcie.setServiceIp(ip);
                    providerServcie.setServicePort(Integer.valueOf(port));
                    providerServcieList.add(providerServcie);
                }

                zkClient.subscribeChildChanges(providerPath, new IZkChildListener() {
                    @Override
                    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                        currentChilds = currentChilds == null ? Lists.newArrayList() : currentChilds;
                        List<String> activityServiceIpList = Lists.transform(currentChilds, new Function<String, String>() {
                            @Override
                            public String apply(String input) {
                                return Splitter.on("|").splitToList(input).get(0);
                            }
                        });
                        refreshProviderServiceMap4Invoker(activityServiceIpList);
                    }
                });
            }
        }
        return currentProviderServiceMap4Invoker;
    }

    @Override
    public void initProviderServiceMap4Invoker() {
        if (MapUtils.isEmpty(providerServiceMap4Invoker)) {
            Map<String, List<ProviderServcie>> providerServiceMetaDataMap = fetchOrUpdateProviderServiceMap4Invoker();
            providerServiceMap4Invoker.putAll(providerServiceMetaDataMap);
        }
    }

    @Override
    public Map<String, List<ProviderServcie>> getProviderServiceMap4Invoker() {
        return providerServiceMap4Invoker;
    }

    @Override
    public void registerInvoker(InvokerService invoker) {
        if (invoker == null) {
            return;
        }

        if (zkClient == null) {
            zkClient = new ZkClient(ZK_SERVER, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT, new SerializableSerializer());
        }

        if (!zkClient.exists(APP_PATH)) {
            zkClient.createPersistent(APP_PATH, true);
        }

        String serviceItf = invoker.getServiceItf().getName();
        String invokerPath = ROOT_PATH + "/" + serviceItf + INVOKER_TYPE;

        if (!zkClient.exists(invokerPath)) {
            zkClient.createPersistent(invokerPath, true);
        }

        String ip = "127.0.0.1";
        String ipPath = invokerPath + "/" + ip;

        if (!zkClient.exists(ipPath)) {
            zkClient.createEphemeral(ipPath);
        }
    }

    private void refreshProviderServiceMap4Invoker(List<String> ipList) {

        ipList = ipList == null ? Lists.newArrayList() : ipList;

        Map<String, List<ProviderServcie>> currentProviderServiceMap4Invoker = Maps.newHashMap();

        for (Map.Entry<String, List<ProviderServcie>> entry : providerServiceMap4Invoker.entrySet()) {
            String serivceItfKey = entry.getKey();
            List<ProviderServcie> providerServcieList = currentProviderServiceMap4Invoker.get(serivceItfKey);
            if (CollectionUtils.isEmpty(providerServcieList)) {
                providerServcieList = Lists.newArrayList();
                currentProviderServiceMap4Invoker.put(serivceItfKey, providerServcieList);
            }

            List<ProviderServcie> serviceList = entry.getValue();
            for (ProviderServcie serviceMetaData : serviceList) {
                if (ipList.contains(serviceMetaData.getServiceIp())) {
                    providerServcieList.add(serviceMetaData);
                }
            }
        }

        providerServiceMap4Invoker.putAll(currentProviderServiceMap4Invoker);
    }
}
