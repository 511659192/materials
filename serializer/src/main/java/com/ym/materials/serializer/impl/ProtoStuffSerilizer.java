package com.ym.materials.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.google.common.collect.Maps;
import com.sun.org.apache.regexp.internal.RE;
import com.ym.materials.serializer.Entity;
import com.ym.materials.serializer.ISerializer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.Map;


/**
 * Created by ym on 2018/5/20.
 */
public class ProtoStuffSerilizer implements ISerializer {

    private static Map<Class<?>, Schema<?>> cached = Maps.newConcurrentMap();

    private static Objenesis objenesis = new ObjenesisStd(true);

    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) cached.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.<T>createFrom(clazz);
            cached.put(clazz, schema);
        }
        return schema;
    }

    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        Class<T> clazz = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        Schema<T> schema = getSchema(clazz);
        byte[] bytes = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        T message = objenesis.newInstance(clazz);
        Schema<T> schema = getSchema(clazz);
        ProtostuffIOUtil.mergeFrom(bytes, message, schema);
        return message;
    }

    public static void main(String[] args) throws Exception {
        Entity entity = new Entity(new Date(), "entity");
        ISerializer serilizer = new ProtoStuffSerilizer();
        byte[] bytes = serilizer.serialize(entity);
        System.out.println(bytes.length);
        System.out.println(new String(bytes));
        Entity result = serilizer.deserialize(bytes, Entity.class);
        System.out.println(JSON.toJSONString(result));
    }
}
