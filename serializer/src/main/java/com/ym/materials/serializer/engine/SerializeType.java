package com.ym.materials.serializer.engine;

import java.util.Objects;

/**
 * Created by ym on 2018/5/20.
 */
public enum SerializeType {
    JdkSerializer,
    HessianSerializer,
    FastJsonSerializer,
    JacksonSerializer,
    JBossSerializer,
    XmlSerializer,
    Xml2Serializer,
    ProtoStuffSerilizer,
    ;

    public static SerializeType getSerializeType(String name) {
        for (SerializeType serializeType : SerializeType.values()) {
            if (Objects.equals(name, serializeType.name())) {
                return serializeType;
            }
        }
        return null;
    }
}
