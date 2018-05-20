package com.ym.materials.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ym.materials.serializer.Entity;
import com.ym.materials.serializer.ISerializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ym on 2018/5/20.
 */
public class JacksonSerializer implements ISerializer {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        SimpleModule module = new SimpleModule("DatetimeModule", Version.unknownVersion());
        module.addSerializer(Date.class, new FDateJsonSerializer());
        module.addDeserializer(Date.class, new FDateJsonDeserializer());
        objectMapper.registerModule(module);
    }

    private static ObjectMapper getObjectMapperInstance() {
        return objectMapper;
    }

    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        if (obj == null)
            return new byte[0];

        String json = objectMapper.writeValueAsString(obj);
        return json.getBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        String json = new String(bytes);
        T result = null;
        result = objectMapper.readValue(json, clazz);
        return result;
    }

    public static void main(String[] args) throws Exception {
        Entity entity = new Entity(new Date(), "entity");
        JacksonSerializer ser = new JacksonSerializer();
        byte[] bytes = ser.serialize(entity);
        System.out.println(new String(bytes));
        Entity result = ser.deserialize(bytes, Entity.class);
        System.out.println(JSON.toJSONString(result));
    }
}

class FDateJsonSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        jsonGenerator.writeString(StringUtils.isNotBlank(dateStr) ? dateStr : "null");
    }
}

class FDateJsonDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String text = jsonParser.getText();
        if (StringUtils.isBlank(text)) {
            return null;
        }

        if (StringUtils.isNumeric(text)) {
            return new Date(Long.valueOf(text));
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
