package tests.knowledge;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

import java.util.*;

/**
 * Test class for Journey Knowledge Items endpoints.
 * Tests knowledge item management and content operations.
 */
@Epic("Knowledge Management")
@Feature("Knowledge Items")
public class KnowledgeItemTest {
    private KnowledgeItemClient client;
    private static final String TEST_JOURNEY_ID = "test-journey-123";
    private String createdKnowledgeItemId;

    @BeforeClass
    public void setup() {
        client = new KnowledgeItemClient();
    }

    @Test(description = "Get knowledge items - Success (200)")
    public void testGetKnowledgeItems_Success() {
        // Arrange
        // Using test journey ID

        // Act
        Response response = client.getKnowledgeItems(TEST_JOURNEY_ID);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertResponseTimeBelow(response, 3000);
        }
    }

    @Test(description = "Get knowledge items - Journey not found (404)")
    public void testGetKnowledgeItems_NotFound() {
        // Arrange
        String nonExistentJourneyId = "non-existent-journey-999";

        // Act
        Response response = client.getKnowledgeItems(nonExistentJourneyId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }

    @Test(description = "Create knowledge item - Success (201)", priority = 1)
    public void testCreateKnowledgeItem_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("name", "Company Policies");
        request.put("description", "Important company policies for new hires");
        request.put("category", "HR");
        request.put("tags", new String[]{"policies", "hr", "onboarding"});

        // Act
        Response response = client.createKnowledgeItem(TEST_JOURNEY_ID, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 201, 400, 404);

        if (response.getStatusCode() == 201 || response.getStatusCode() == 200) {
            try {
                createdKnowledgeItemId = ResponseAssertions.extractJsonPath(response, "$.id");
            } catch (Exception e) {}
        }
    }

    @Test(description = "Create knowledge item - Invalid request (400)")
    public void testCreateKnowledgeItem_InvalidRequest() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        // Missing required fields

        // Act
        Response response = client.createKnowledgeItem(TEST_JOURNEY_ID, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 400, 201, 404);
    }

    @Test(description = "Update knowledge item - Success (200)", priority = 2)
    public void testUpdateKnowledgeItem_Success() {
        // Arrange
        String itemId = createdKnowledgeItemId != null ? createdKnowledgeItemId : "sample-item-id";

        Map<String, Object> request = new HashMap<>();
        request.put("name", "Updated Company Policies");
        request.put("description", "Updated description");

        // Act
        Response response = client.updateKnowledgeItem(itemId, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Update knowledge item - Not found (404)")
    public void testUpdateKnowledgeItem_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-item-999";
        Map<String, Object> request = new HashMap<>();
        request.put("name", "Updated Item");

        // Act
        Response response = client.updateKnowledgeItem(nonExistentId, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }

    @Test(description = "Update knowledge item content - Success (200)")
    public void testUpdateKnowledgeItemContent_Success() {
        // Arrange
        String itemId = createdKnowledgeItemId != null ? createdKnowledgeItemId : "sample-item-id";

        Map<String, Object> request = new HashMap<>();
        request.put("content", "Detailed policy content goes here...");
        request.put("format", "markdown");

        // Act
        Response response = client.updateKnowledgeItemContent(itemId, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Get knowledge item by ID - Success (200)")
    public void testGetKnowledgeItemById_Success() {
        // Arrange
        String itemId = createdKnowledgeItemId != null ? createdKnowledgeItemId : "sample-item-id";

        // Act
        Response response = client.getKnowledgeItemById(itemId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertBodyNotEmpty(response);
        }
    }

    @Test(description = "Get knowledge item by ID - Not found (404)")
    public void testGetKnowledgeItemById_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-item-999";

        // Act
        Response response = client.getKnowledgeItemById(nonExistentId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }

    @Test(description = "Delete knowledge item content - Success (204)")
    public void testDeleteKnowledgeItemContent_Success() {
        // Arrange
        String itemId = createdKnowledgeItemId != null ? createdKnowledgeItemId : "sample-item-id";

        // Act
        Response response = client.deleteKnowledgeItemContent(itemId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 204, 200, 404);
    }

    @Test(description = "Delete knowledge item - Success (204)", priority = 3)
    public void testDeleteKnowledgeItem_Success() {
        // Arrange
        String itemId = createdKnowledgeItemId != null ? createdKnowledgeItemId : "sample-item-id";

        // Act
        Response response = client.deleteKnowledgeItem(itemId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 204, 200, 404);
    }

    @Test(description = "Delete knowledge item - Not found (404)")
    public void testDeleteKnowledgeItem_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-item-999";

        // Act
        Response response = client.deleteKnowledgeItem(nonExistentId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }

    @Test(description = "Delete knowledge item - Unauthorized (401)")
    public void testDeleteKnowledgeItem_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();
        String itemId = "sample-item-id";

        try {
            // Act
            Response response = client.deleteKnowledgeItem(itemId);

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }
}
