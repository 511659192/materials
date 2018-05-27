package com.ym.materials.customProxyFactoryBean.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ym on 2018/5/27.
 */
public class PropertiesUtil {

    protected static Properties load(String path) {

        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new RuntimeException("file not found " + path);
        }
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("properties local error exception", e);
        }
        return properties;
    }
}
