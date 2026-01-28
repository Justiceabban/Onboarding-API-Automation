package utils;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.*;

/**
 * JSON utility class for serialization and deserialization.
 * Uses Jackson ObjectMapper with custom configuration.
 */
public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // Configure ObjectMapper
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    /**
     * Deserialize JSON string to Java object.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON to " + clazz.getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Serialize Java object to JSON string.
     */
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Pretty print JSON string.
     */
    public static String toPrettyJson(Object obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to pretty JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Get ObjectMapper instance.
     */
    public static ObjectMapper getMapper() {
        return mapper;
    }
}
