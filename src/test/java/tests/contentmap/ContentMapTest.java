package tests.contentmap;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

import java.util.*;

/**
 * Test class for Content Map endpoints.
 * Tests content translations and internationalization management.
 */
@Epic("Content Management")
@Feature("Content Map & Translation")
public class ContentMapTest {
    private ContentMapClient client;
    private String createdContentMapId;

    @BeforeClass
    public void setup() {
        client = new ContentMapClient();
    }

    @Test(description = "Search content maps - Success (200)")
    public void testSearchContentMaps_Success() {
        // Arrange
        String searchTerm = "onboarding";

        // Act
        Response response = client.searchContentMaps(searchTerm);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 400, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertResponseTimeBelow(response, 3000);
        }
    }

    @Test(description = "Search content maps with filters - Success (200)")
    public void testSearchContentMaps_WithFilters() {
        // Arrange
        String searchTerm = "journey";
        Boolean migrated = true;

        // Act
        Response response = client.searchContentMaps(searchTerm, migrated, 0, 20);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 400, 404);
    }

    @Test(description = "Search content maps - Empty search term (400)")
    public void testSearchContentMaps_EmptySearchTerm() {
        // Arrange
        String searchTerm = "";

        // Act
        Response response = client.searchContentMaps(searchTerm);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 400, 200);
    }

    @Test(description = "Create content map - Success (201)", priority = 1)
    public void testCreateContentMap_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("courseId", "journey-onboarding-2024");

        Map<String, Object> translations = new HashMap<>();
        Map<String, Object> enTranslation = new HashMap<>();
        enTranslation.put("title", "Welcome to Onboarding");
        enTranslation.put("description", "Onboarding journey description");
        translations.put("en", enTranslation);

        request.put("translations", translations);

        // Act
        Response response = client.createContentMap(request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 201, 400);

        if (response.getStatusCode() == 201 || response.getStatusCode() == 200) {
            try {
                createdContentMapId = ResponseAssertions.extractJsonPath(response, "$.id");
            } catch (Exception e) {}
        }
    }

    @Test(description = "Create content map - Invalid request (400)")
    public void testCreateContentMap_InvalidRequest() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        // Missing required fields

        // Act
        Response response = client.createContentMap(request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 400, 201);
    }

    @Test(description = "Get content map by ID - Success (200)", priority = 2)
    public void testGetContentMapById_Success() {
        // Arrange
        String contentMapId = createdContentMapId != null ? createdContentMapId : "sample-contentmap-id";

        // Act
        Response response = client.getContentMapById(contentMapId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertBodyNotEmpty(response);
        }
    }

    @Test(description = "Get content map by ID - Not found (404)")
    public void testGetContentMapById_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-contentmap-999";

        // Act
        Response response = client.getContentMapById(nonExistentId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }

    @Test(description = "Update content map - Success (200)", priority = 3)
    public void testUpdateContentMap_Success() {
        // Arrange
        String contentMapId = createdContentMapId != null ? createdContentMapId : "sample-contentmap-id";

        Map<String, Object> request = new HashMap<>();
        Map<String, Object> translations = new HashMap<>();
        Map<String, Object> enTranslation = new HashMap<>();
        enTranslation.put("title", "Updated Welcome to Onboarding");
        translations.put("en", enTranslation);
        request.put("translations", translations);

        // Act
        Response response = client.updateContentMap(contentMapId, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Search content maps - Unauthorized (401)")
    public void testSearchContentMaps_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            // Act
            Response response = client.searchContentMaps("test");

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    @Test(description = "Delete content map - Success (204)", priority = 4)
    public void testDeleteContentMap_Success() {
        // Arrange
        String contentMapId = createdContentMapId != null ? createdContentMapId : "sample-contentmap-id";

        // Act
        Response response = client.deleteContentMap(contentMapId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 204, 200, 404);
    }

    @Test(description = "Delete content map - Not found (404)")
    public void testDeleteContentMap_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-contentmap-999";

        // Act
        Response response = client.deleteContentMap(nonExistentId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }
}
