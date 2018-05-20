package com.ym.materials.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ym.materials.serializer.Entity;
import com.ym.materials.serializer.ISerializer;

import java.util.Date;

/**
 * Created by ym on 2018/5/20.
 */
public class FastjsonSerializer implements ISerializer {
    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        return JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        String text = new String(bytes);
        T t = JSON.parseObject(text, clazz);
        return t;
    }

    public static void main(String[] args) throws Exception   {
        Entity entity = new Entity(new Date(), "entity");
        FastjsonSerializer ser = new FastjsonSerializer();
        byte[] bytes = ser.serialize(entity);
        System.out.println(bytes.length);
        System.out.println(new String(bytes));
        Entity result = ser.deserialize(bytes, Entity.class);
        System.out.println(JSON.toJSONString(result));
    }
}
