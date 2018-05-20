package com.ym.materials.serializer.impl;

import com.ym.materials.serializer.ISerializer;

import java.io.*;

/**
 * Created by ym on 2018/5/20.
 */
public class JdkSerializer implements ISerializer {
    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Object object = objectInputStream.readObject();
        return (T) object;
    }
}


class SerializeTransientField implements Serializable {

    private transient String name = "name";

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(name);
    }

    private void readObject(ObjectInputStream in) throws Exception {
        in.defaultReadObject();
        in.readObject();
    }
}
