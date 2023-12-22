package main.java.com.jdreamwalker.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TransformUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T convertValue(final Object object, final Class<T> toClass) {
        return objectMapper.convertValue(object, toClass);
    }

    public static Object fromJson(String jsonString, Class<?> valueType) {
        try {
            if (jsonString != null) {
                return objectMapper.readValue(jsonString, valueType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

