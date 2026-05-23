package com.antiphishing.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JsonHelper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> parseFeatures(String featuresJson) {
        try {
            return objectMapper.readValue(featuresJson, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return Map.of("error", "Failed to parse features");
        }
    }
}