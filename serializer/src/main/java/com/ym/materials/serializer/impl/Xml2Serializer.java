package com.ym.materials.serializer.impl;

import com.ym.materials.serializer.ISerializer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by ym on 2018/5/20.
 */
public class Xml2Serializer implements ISerializer {

    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(out, "utf-8", true, 0);
        encoder.writeObject(obj);
        encoder.close();
        return out.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        XMLDecoder decoder = new XMLDecoder(in);
        Object object = decoder.readObject();
        decoder.close();
        return (T) object;
    }
}
