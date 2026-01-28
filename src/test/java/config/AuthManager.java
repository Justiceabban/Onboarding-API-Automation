package config;

/**
 * Authentication manager for handling bearer tokens.
 * Supports dynamic token management and injection into API requests.
 * Supports multiple user types (Admin, Editor, Viewer) with their own tokens.
 */
public class AuthManager {
    private static ThreadLocal<String> bearerToken = new ThreadLocal<>();
    private static ThreadLocal<Boolean> isCleared = new ThreadLocal<>();
    private static ThreadLocal<UserType> currentUserType = new ThreadLocal<>();

    static {
        // Initialize with token from config
        bearerToken.set(EnvironmentConfig.getBearerToken());
        isCleared.set(false);
    }

    /**
     * Set bearer token for the current thread.
     */
    public static void setBearerToken(String token) {
        bearerToken.set(token);
        isCleared.set(false);
        currentUserType.remove(); // Clear user type when setting custom token
    }

    /**
     * Set user type and automatically load their token from config.
     * @param userType The type of user (ADMIN, EDITOR, VIEWER)
     */
    public static void setUserType(UserType userType) {
        currentUserType.set(userType);
        bearerToken.set(EnvironmentConfig.getBearerToken(userType));
        isCleared.set(false);
    }

    /**
     * Get the current user type.
     * @return Current user type or null if not set
     */
    public static UserType getCurrentUserType() {
        return currentUserType.get();
    }

    /**
     * Get bearer token for the current thread.
     */
    public static String getBearerToken() {
        // If explicitly cleared, return null
        if (Boolean.TRUE.equals(isCleared.get())) {
            return null;
        }

        String token = bearerToken.get();
        return token != null ? token : EnvironmentConfig.getBearerToken();
    }

    /**
     * Get bearer token for a specific user type without changing the current user.
     * @param userType The type of user
     * @return Bearer token for the specified user type
     */
    public static String getBearerToken(UserType userType) {
        return EnvironmentConfig.getBearerToken(userType);
    }

    /**
     * Clear bearer token for the current thread.
     */
    public static void clearBearerToken() {
        bearerToken.remove();
        currentUserType.remove();
        isCleared.set(true);
    }

    /**
     * Reset to default token from config.
     */
    public static void resetToDefault() {
        bearerToken.set(EnvironmentConfig.getBearerToken());
        currentUserType.remove();
        isCleared.set(false);
    }

    /**
     * Check if bearer token is set.
     */
    public static boolean hasToken(UserType userType) {
        return getBearerToken(userType) != null && !getBearerToken(userType).isEmpty();
    }
}
