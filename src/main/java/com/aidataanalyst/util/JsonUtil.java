package com.aidataanalyst.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
    }

    private JsonUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> String objectToJson(final T object) {
        Objects.requireNonNull(object);
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> T jsonToObject(final String json, final Class<T> clazz) {
        Objects.requireNonNull(json);
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        try {
            // Convert JSON array to array of T
            Class<T[]> arrayClazz = (Class<T[]>) Array.newInstance(clazz, 0).getClass();
            T[] array = mapper.readValue(json, arrayClazz);
            // Convert array to list
            return List.of(array);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> T jsonNodeToObject(final JsonNode json, final Class<T> clazz) {
        Objects.requireNonNull(json);
        try {
            return mapper.treeToValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static JsonNode jsonToJsonNode(final String json) {
        Objects.requireNonNull(json);
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> JsonNode objectToJsonNode(final T object) {
        Objects.requireNonNull(object);
        return mapper.valueToTree(object);
    }

    public static ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }


    public static ObjectNode empty() {
        return mapper.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }

    public static <T> T toMap(final MapType mapType, final String json) {
        Objects.requireNonNull(json);
        try {
            return mapper.readValue(json, mapType);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <K, V> Map<K, V> toMap(
            final Class<? extends Map> mapClass,
            final Class<K> keyClass,
            final Class<V> valueClass,
            final String jsonString) {
        MapType mapType = mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
        try {
            return mapper.readValue(jsonString, mapType);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
