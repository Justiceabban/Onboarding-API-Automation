package config;

import java.io.*;
import java.util.*;

/**
 * Environment configuration manager.
 * Loads and manages configuration properties for different environments (dev, test, stage).
 */
public class EnvironmentConfig {
    private static final String CONFIG_FILE = "src/test/resources/config.properties";
    private static Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = EnvironmentConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                // Fallback to file system
                try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
                    properties.load(fis);
                }
            } else {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage(), e);
        }
    }

    /**
     * Get property value by key.
     */
    public static String get(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key);
        }
        return value;
    }

    /**
     * Get base URL for the current environment.
     */
    public static String getBaseUrl() {
        String env = get("environment");
        if (env == null) {
            env = "dev";
        }
        return get(env + ".baseUrl");
    }

    /**
     * Get bearer token for authentication.
     */
    public static String getBearerToken() {
        return get("bearerToken");
    }

    /**
     * Get bearer token for a specific user type.
     * @param userType The type of user (ADMIN, EDITOR, VIEWER)
     * @return Bearer token for the specified user type
     */
    public static String getBearerToken(UserType userType) {
        String tokenKey = userType.getRole() + ".bearerToken";
        String token = get(tokenKey);
        // Fallback to default token if specific token not found
        return token != null ? token : getBearerToken();
    }

    /**
     * Get current environment name.
     */
    public static String getEnvironment() {
        return get("environment");
    }
}
