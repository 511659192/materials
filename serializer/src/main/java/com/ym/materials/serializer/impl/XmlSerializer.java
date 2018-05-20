package com.ym.materials.serializer.impl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.ym.materials.serializer.ISerializer;

/**
 * Created by ym on 2018/5/20.
 */
public class XmlSerializer implements ISerializer {

    private final static XStream xstream = new XStream(new DomDriver());
    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        return xstream.toXML(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        return (T) xstream.fromXML(new String(bytes));
    }
}
