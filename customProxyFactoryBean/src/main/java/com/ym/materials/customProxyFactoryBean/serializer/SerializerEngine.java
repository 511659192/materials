package com.ym.materials.customProxyFactoryBean.serializer;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by ym on 2018/5/20.
 */
public class SerializerEngine {

    private final static Map<SerializeType, ISerializer> map = Maps.newHashMap();

    static {
//        map.put(SerializeType.JdkSerializer, new JdkSerializer());
//        map.put(SerializeType.HessianSerializer, new HessianSerializer());
//        map.put(SerializeType.FastJsonSerializer, new FastjsonSerializer());
//        map.put(SerializeType.JacksonSerializer, new JacksonSerializer());
//        map.put(SerializeType.JBossSerializer, new JBossSerializer());
//        map.put(SerializeType.XmlSerializer, new XmlSerializer());
//        map.put(SerializeType.Xml2Serializer, new Xml2Serializer());
//        map.put(SerializeType.ProtoStuffSerilizer, new ProtoStuffSerilizer());
    }


    public static <T> byte[] serialize(T obj, String type) throws Exception {
        SerializeType serializeType = SerializeType.getSerializeType(type);
        if (type == null) {
            throw new RuntimeException("type in invalid");
        }

        ISerializer serializer = map.get(serializeType);
        return serializer.serialize(obj);
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz, String type) throws Exception {
        SerializeType serializeType = SerializeType.getSerializeType(type);
        if (type == null) {
            throw new RuntimeException("type in invalid");
        }

        ISerializer serializer = map.get(serializeType);
        return serializer.deserialize(bytes, clazz);
    }
}
