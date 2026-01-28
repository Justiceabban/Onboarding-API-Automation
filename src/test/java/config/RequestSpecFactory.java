package config;

import io.qameta.allure.restassured.*;
import io.restassured.builder.*;
import io.restassured.filter.log.*;
import io.restassured.http.*;
import io.restassured.specification.*;

/**
 * Factory for creating Rest Assured request specifications.
 * Provides reusable request configuration with authentication and common headers.
 * Supports multiple user types (Admin, Editor, Viewer).
 */
public class RequestSpecFactory {

    /**
     * Get base request specification with authentication and Allure reporting.
     */
    public static RequestSpecification getAdminRequestSpec() {
        return getUserRequestSpec(UserType.ADMIN);
    }

    public static RequestSpecification getEditorRequestSpec() {
        return getUserRequestSpec(UserType.EDITOR);
    }

    public static RequestSpecification getViewerRequestSpec() {
        return getUserRequestSpec(UserType.VIEWER);
    }

    public static RequestSpecification getUserRequestSpec(UserType userType) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(EnvironmentConfig.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured()) // Add Allure filter for request/response logging
                .log(LogDetail.URI)
                .log(LogDetail.METHOD);

        // Add authorization header if token is available
        String token = AuthManager.getBearerToken(userType);
        if (AuthManager.hasToken(userType)) {
            builder.addHeader("Authorization", "Bearer " + token);
        }
        return builder.build();
    }

    /**
     * Get request specification without authentication.
     */
    public static RequestSpecification getRequestSpecWithoutAuth() {
        return new RequestSpecBuilder()
                .setBaseUri(EnvironmentConfig.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured()) // Add Allure filter
                .log(LogDetail.URI)
                .log(LogDetail.METHOD)
                .build();
    }
}
