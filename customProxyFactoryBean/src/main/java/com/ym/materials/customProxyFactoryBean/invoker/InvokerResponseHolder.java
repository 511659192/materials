package com.ym.materials.customProxyFactoryBean.invoker;

import com.google.common.collect.Maps;
import com.ym.materials.customProxyFactoryBean.entity.AresResponse;
import com.ym.materials.customProxyFactoryBean.entity.AresResponseWrapper;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by ym on 2018/5/27.
 */
public class InvokerResponseHolder {

    private final static Map<String, AresResponseWrapper> responseMap = Maps.newConcurrentMap();

    private final static ExecutorService removeExpireKeyExecutor = Executors.newSingleThreadExecutor();

    static {
        removeExpireKeyExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (Map.Entry<String, AresResponseWrapper> entry : responseMap.entrySet()) {
                        AresResponseWrapper responseWrapper = entry.getValue();
                        if (responseWrapper.isExpire()) {
                            responseMap.remove(entry.getKey());
                        }
                        try {
                            TimeUnit.MICROSECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    public static void initResponseData(String requestUniqueKey) {
        responseMap.put(requestUniqueKey, AresResponseWrapper.of());
    }

    public static void putResultValue(AresResponse response) {
        long time = System.currentTimeMillis();
        AresResponseWrapper responseWrapper = responseMap.get(response.getUniqueKey());
        responseWrapper.setReponseTime(time);
        responseWrapper.getResponseQueue().add(response);
    }

    public static AresResponse getValue(String requestUniqueKey, long timeout) {
        AresResponseWrapper responseWrapper = responseMap.get(requestUniqueKey);
        try {
            AresResponse response = responseWrapper.getResponseQueue().poll(timeout, TimeUnit.MICROSECONDS);
            return response;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            responseMap.remove(requestUniqueKey);
        }
    }
}
