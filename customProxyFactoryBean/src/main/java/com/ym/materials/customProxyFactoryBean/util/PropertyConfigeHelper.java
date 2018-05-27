package com.ym.materials.customProxyFactoryBean.util;

import com.ym.materials.customProxyFactoryBean.serializer.SerializeType;

import java.util.Properties;

/**
 * Created by ym on 2018/5/24.
 */
public class PropertyConfigeHelper {
    private static String ZKService;
    private static int ZKSessionTimeout;
    private static int ZKConnectionTimeout;
    private static String appName;
    private static int channelConnectSize;
    private static SerializeType serializeType;

    private static Properties properties;

    static {
        properties = PropertiesUtil.load("config.properties");
    }

    public static SerializeType getSerializeType() {
        if (serializeType == null) {
            String serializeTypeName = properties.getProperty("conf.serializeType");
            serializeType = SerializeType.getSerializeType(serializeTypeName);
        }
        return serializeType;
    }

    public static String getZKService() {
        if (ZKService == null) {
            ZKService = properties.getProperty("conf.ZKService");
        }
        return ZKService;
    }

    public static void setZKService(String ZKService) {
        PropertyConfigeHelper.ZKService = ZKService;
    }

    public static int getZKSessionTimeout() {
        if (ZKSessionTimeout <= 0) {
            ZKSessionTimeout = Integer.valueOf(properties.getProperty("conf.ZKSessionTimeout"));
        }
        return ZKSessionTimeout;
    }

    public static void setZKSessionTimeout(int ZKSessionTimeout) {
        PropertyConfigeHelper.ZKSessionTimeout = ZKSessionTimeout;
    }

    public static int getZKConnectionTimeout() {
        if (ZKConnectionTimeout <= 0) {
            ZKConnectionTimeout = Integer.valueOf(properties.getProperty("conf.ZKConnectionTimeout"));
        }
        return ZKConnectionTimeout;
    }

    public static void setZKConnectionTimeout(int ZKConnectionTimeout) {
        PropertyConfigeHelper.ZKConnectionTimeout = ZKConnectionTimeout;
    }

    public static String getAppName() {
        if (appName == null) {
            appName = properties.getProperty("conf.appName");
        }
        return appName;
    }

    public static int getChannelConnectSize() {
        if (channelConnectSize <= 0) {
            channelConnectSize = Integer.valueOf(properties.getProperty("conf.channelConnectSize"));
        }
        return channelConnectSize;
    }

    public static void setAppName(String appName) {
        PropertyConfigeHelper.appName = appName;
    }

    public static void setChannelConnectSize(int channelConnectSize) {
        PropertyConfigeHelper.channelConnectSize = channelConnectSize;
    }

    public static void setSerializeType(SerializeType serializeType) {
        PropertyConfigeHelper.serializeType = serializeType;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        PropertyConfigeHelper.properties = properties;
    }
}
