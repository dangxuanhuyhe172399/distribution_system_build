package com.sep490.bads.distributionsystem.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("JSON parsing error", e);
        }
    }

    public static <T> T entity(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (Exception e) {
            throw new RuntimeException("JSON parsing error", e);
        }
    }

    public static <T> ArrayList<T> arrayList(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass));
        } catch (Exception e) {
            throw new RuntimeException("JSON parsing error", e);
        }
    }

    public static <T> Page<T> toPage(String json, Class<T> tClass) {
        try {
            List<T> content = mapper.readValue(json, new TypeReference<List<T>>() {});
            return new PageImpl<>(content, PageRequest.of(0, content.size()), content.size());
        } catch (Exception e) {
            throw new RuntimeException("JSON parsing error", e);
        }
    }
}
