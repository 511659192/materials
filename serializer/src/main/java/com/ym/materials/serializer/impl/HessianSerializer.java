package com.ym.materials.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.ym.materials.serializer.Entity;
import com.ym.materials.serializer.ISerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by ym on 2018/5/20.
 */
public class HessianSerializer implements ISerializer {
    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        if (obj == null) {
            throw new RuntimeException("obj is null");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(out);
        hessianOutput.writeObject(obj);
        return out.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        HessianInput hessianInput = new HessianInput(new ByteArrayInputStream(bytes));
        Object result = hessianInput.readObject();
        return (T) result;
    }

    public static void main(String[] args) throws Exception   {
        Entity entity = new Entity(new Date(), "entity");
        ISerializer serializer = new HessianSerializer();
        byte[] bytes = serializer.serialize(entity);
        System.out.println(bytes.length);
        System.out.println(new String(bytes));
        Entity result = serializer.deserialize(bytes, Entity.class);
        System.out.println(JSON.toJSONString(result));
    }
}
