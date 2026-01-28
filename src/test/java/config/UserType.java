package config;

/**
 * Enum representing different user types in the system.
 * Each user type can have its own authentication token.
 */
public enum UserType {
    ADMIN("admin"),
    EDITOR("editor"),
    VIEWER("viewer");

    private final String role;

    UserType(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
