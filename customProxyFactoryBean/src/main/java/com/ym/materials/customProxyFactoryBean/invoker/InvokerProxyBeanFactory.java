package com.ym.materials.customProxyFactoryBean.invoker;

import com.ym.materials.customProxyFactoryBean.cluster.ClusterEngine;
import com.ym.materials.customProxyFactoryBean.cluster.ClusterStrategy;
import com.ym.materials.customProxyFactoryBean.entity.AresRequest;
import com.ym.materials.customProxyFactoryBean.entity.AresResponse;
import com.ym.materials.customProxyFactoryBean.provider.ProviderServcie;
import com.ym.materials.customProxyFactoryBean.register.IRegisterCenter4Invoker;
import com.ym.materials.customProxyFactoryBean.register.RegisterCenter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by ym on 2018/5/25.
 */
public class InvokerProxyBeanFactory implements InvocationHandler {
    private ExecutorService executorService = null;
    private Class<?> targetInterface;
    private int timeout;
    private static int threadWorkerNumber = 10;
    private String clusterStrategy;
    private static volatile InvokerProxyBeanFactory singleton;

    public InvokerProxyBeanFactory(Class<?> targetInterface, int timeout, String clusterStrategy) {
        this.targetInterface = targetInterface;
        this.timeout = timeout;
        this.clusterStrategy = clusterStrategy;
    }

    public static InvokerProxyBeanFactory singleton(Class<?> targetInterface, int timeout, String clusterStrategy) {
        if (singleton == null) {
            singleton = new InvokerProxyBeanFactory(targetInterface, timeout, clusterStrategy);
        }
        return singleton;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String serviceKey = targetInterface.getName();
        IRegisterCenter4Invoker registerCenter4Invoker = RegisterCenter.singleton();
        List<ProviderServcie> providerServcieList = registerCenter4Invoker.getProviderServiceMap4Invoker().get(serviceKey);
        ClusterStrategy clusterStategy = ClusterEngine.getClusterStategy(clusterStrategy);
        ProviderServcie providerServcie = clusterStategy.select(providerServcieList);
        ProviderServcie newProvicerService = providerServcie.copy();
        newProvicerService.setServiceMethod(method);
        newProvicerService.setServiceItf(targetInterface);
        AresRequest request = new AresRequest();
        request.setUniqueKey(UUID.randomUUID().toString() + "-" + Thread.currentThread().getId());
        request.setProviderService(providerServcie);
        request.setInvokeMethodName(method.getName());
        request.setArgs(args);
        try {
            if (executorService == null) {
                synchronized (InvokerProxyBeanFactory.class) {
                    if (executorService == null) {
                        executorService = Executors.newFixedThreadPool(threadWorkerNumber);
                    }
                }
            }

            String serviceIp = providerServcie.getServiceIp();
            Integer servicePort = providerServcie.getServicePort();
            InetSocketAddress socketAddress = new InetSocketAddress(serviceIp, servicePort);
            Future<AresResponse> responseFuture = executorService.submit(new InvokerServiceCallable(socketAddress, request));
            AresResponse aresResponse = responseFuture.get(request.getInvokeTimeout(), TimeUnit.MICROSECONDS);
            if (aresResponse != null) {
                return aresResponse;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{targetInterface},
                this);
    }
}
