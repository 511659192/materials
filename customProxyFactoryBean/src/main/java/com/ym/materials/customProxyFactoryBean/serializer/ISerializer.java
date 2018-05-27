package com.ym.materials.customProxyFactoryBean.serializer;

/**
 * Created by ym on 2018/5/20.
 */
public interface ISerializer {

    <T> byte[] serialize(T obj) throws Exception;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception;
}
