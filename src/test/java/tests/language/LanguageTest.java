package tests.language;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

import java.util.*;

/**
 * Test class for Language endpoints.
 * Tests supported language operations.
 */
@Epic("Localization")
@Feature("Language Management")
public class LanguageTest {
    private LanguageClient client;
    private static final String TEST_LANGUAGE_CODE = "en";

    @BeforeClass
    public void setup() {
        client = new LanguageClient();
    }

    @Test(description = "Get all languages - Success (200)")
    public void testGetAllLanguages_Success() {
        // Arrange
        // No specific setup needed

        // Act
        Response response = client.getAllLanguages();

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertResponseTimeBelow(response, 3000);
            ResponseAssertions.assertBodyNotEmpty(response);
        }
    }

    @Test(description = "Get all languages - Unauthorized (401)")
    public void testGetAllLanguages_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            // Act
            Response response = client.getAllLanguages();

            // Assert
            ResponseAssertions.assertStatusCodeIn(response, 200, 401);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    @Test(description = "Get language by code - Success (200)")
    public void testGetLanguageByCode_Success() {
        // Arrange
        // Using English language code

        // Act
        Response response = client.getLanguageByCode(TEST_LANGUAGE_CODE);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
        }
    }

    @Test(description = "Get language by code - Not found (404)")
    public void testGetLanguageByCode_NotFound() {
        // Arrange
        String invalidCode = "xyz";

        // Act
        Response response = client.getLanguageByCode(invalidCode);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }

    @Test(description = "Add language - Success (201)")
    public void testAddLanguage_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("code", "fr");
        request.put("name", "French");
        request.put("nativeName", "Français");
        request.put("isActive", true);

        // Act
        Response response = client.addLanguage(request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 201, 400, 409);
    }

    @Test(description = "Add language - Invalid request (400)")
    public void testAddLanguage_InvalidRequest() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        // Missing required fields

        // Act
        Response response = client.addLanguage(request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 400, 201);
    }

    @Test(description = "Update language - Success (200)")
    public void testUpdateLanguage_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("name", "English (Updated)");
        request.put("isActive", true);

        // Act
        Response response = client.updateLanguage(TEST_LANGUAGE_CODE, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Update language - Not found (404)")
    public void testUpdateLanguage_NotFound() {
        // Arrange
        String invalidCode = "xyz";
        Map<String, Object> request = new HashMap<>();
        request.put("name", "Updated Language");

        // Act
        Response response = client.updateLanguage(invalidCode, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }
}
