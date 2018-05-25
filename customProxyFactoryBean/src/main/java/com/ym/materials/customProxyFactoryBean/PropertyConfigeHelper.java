package com.ym.materials.customProxyFactoryBean;

/**
 * Created by ym on 2018/5/24.
 */
public class PropertyConfigeHelper {
    private static String ZKService;
    private static int ZKSessionTimeout;
    private static int ZKConnectionTimeout;
    private static String appName;

    public static SerializeType getSerializeType() {
        return null;
    }

    public static String getZKService() {
        return ZKService;
    }

    public static void setZKService(String ZKService) {
        PropertyConfigeHelper.ZKService = ZKService;
    }

    public static int getZKSessionTimeout() {
        return ZKSessionTimeout;
    }

    public static void setZKSessionTimeout(int ZKSessionTimeout) {
        PropertyConfigeHelper.ZKSessionTimeout = ZKSessionTimeout;
    }

    public static int getZKConnectionTimeout() {
        return ZKConnectionTimeout;
    }

    public static void setZKConnectionTimeout(int ZKConnectionTimeout) {
        PropertyConfigeHelper.ZKConnectionTimeout = ZKConnectionTimeout;
    }

    public static String getAppName() {
        return appName;
    }
}
