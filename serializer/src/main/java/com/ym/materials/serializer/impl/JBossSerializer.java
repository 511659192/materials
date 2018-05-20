package com.ym.materials.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.ym.materials.serializer.Entity;
import com.ym.materials.serializer.ISerializer;
import org.jboss.marshalling.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by ym on 2018/5/20.
 */
public class JBossSerializer implements ISerializer {

    private final static MarshallingConfiguration config = new MarshallingConfiguration();

    private final static MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");

    static {
        config.setVersion(5);
    }

    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Marshaller marshaller = factory.createMarshaller(config);
        marshaller.start(Marshalling.createByteOutput(out));
        marshaller.writeObject(obj);
        marshaller.finish();
        return out.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        Unmarshaller unmarshaller = factory.createUnmarshaller(config);
        unmarshaller.start(Marshalling.createByteInput(in));
        T t = unmarshaller.readObject(clazz);
        return t;
    }

    public static void main(String[] args) throws Exception   {
        Entity entity = new Entity(new Date(), "entity");
        ISerializer serializer = new JBossSerializer();
        byte[] bytes = serializer.serialize(entity);
        System.out.println(bytes.length);
        System.out.println(new String(bytes));
        Entity result = serializer.deserialize(bytes, Entity.class);
        System.out.println(JSON.toJSONString(result));
    }
}
