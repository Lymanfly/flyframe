package org.lyman.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Slf4j(topic = "JsonUtils")
public class JsonUtils {

    /**
     * 时区
     */
    private static final TimeZone timeZone = TimeZone.getTimeZone("GMT+8");

    /**
     * 键按自然顺序输出
     *
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        return toJsonString(object, false);
    }

    /**
     * 键按自然顺序格式化输出
     *
     * @param object
     * @param prettyFormat
     * @return
     */
    public static String toJsonString(Object object, boolean prettyFormat) {
        if (object == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 格式化输出
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, prettyFormat);
            // 键按自然顺序输出
            objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
            // 设置时区
            objectMapper.setTimeZone(timeZone);
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 键按自然顺序格式化输出
     *
     * @param object
     * @return
     */
    public static String toJsonStringNonNull(Object object) {
        return toJsonStringNonNull(object, false);
    }


    /**
     * 键按自然顺序格式化输出
     *
     * @param object
     * @param prettyFormat
     * @return
     */
    public static String toJsonStringNonNull(Object object, boolean prettyFormat) {
        if (object == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 格式化输出
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, prettyFormat);
            // 键按自然顺序输出
            objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
            // 为空的序列化
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            // 设置时区
            objectMapper.setTimeZone(timeZone);
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        T res = null;
        try {
            res = objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Can not parse object from json : {}", json, e);
        }
        return res;
    }

    public static <T> List<T> toObjectList(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<T>> reference = new TypeReference<List<T>>() {};
        List<T> res = null;
        try {
            res = objectMapper.readValue(json, reference);
        } catch (JsonProcessingException e) {
            log.error("Source json is not an Array format : {}", json, e);
        }
        return res;
    }

    public static <T> Map<String, T> toStringKeyMap(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, T>> reference = new TypeReference<Map<String, T>>() {};
        Map<String, T> res = null;
        try {
            res = objectMapper.readValue(json, reference);
        } catch (JsonProcessingException e) {
            log.error("Source json is not an Object format : {}", json, e);
        }
        return res;
    }

}
