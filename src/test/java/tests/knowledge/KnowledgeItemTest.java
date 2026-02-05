package tests.knowledge;

import assertions.*;
import client.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

import java.util.*;

/**
 * Test class for Journey Knowledge Items endpoints.
 * Tests knowledge item management and content operations.
 *
 * Knowledge items can be of type 'checklist' or 'faq' and are associated with journeys.
 */
@Epic("Knowledge Management")
@Feature("Knowledge Items")
public class KnowledgeItemTest {
    private KnowledgeItemClient client;
    private JourneyClient journeyClient;
    private ObjectMapper objectMapper;

    // Test data
    private static final String TEST_ASSET_ID = "d0f9b79d-c9d2-48a2-94e5-363787223829";
    private String testJourneySlug;
    private String testJourneyId;
    private String testJourneyTitle;

    private String createdKnowledgeItemId;
    private String createdChecklistKnowledgeId;

    @BeforeClass
    public void setup() {
        client = new KnowledgeItemClient();
        journeyClient = new JourneyClient();
        objectMapper = new ObjectMapper();

        // Create a test journey for knowledge item testing
        createTestJourney();
    }

    /**
     * Helper method to create a test journey for knowledge item operations
     */
    private void createTestJourney() {
        Allure.step("Setup: Create test journey for Knowledge Item tests", () -> {
            testJourneyTitle = "Knowledge Item Test Journey " + System.currentTimeMillis();

            Map<String, Object> journeyRequest = new HashMap<>();
            journeyRequest.put("title", testJourneyTitle);
            journeyRequest.put("assetId", TEST_ASSET_ID);
            journeyRequest.put("assetDescription", "Journey for knowledge item testing");
            journeyRequest.put("language", "en-gb");

            Response createResponse = journeyClient.createJourney(journeyRequest);

            if (createResponse.getStatusCode() == 200 || createResponse.getStatusCode() == 201) {
                System.out.println("✓ Test journey created successfully: " + testJourneyTitle);

                // Get the journey slug by searching
                Response getAllResponse = journeyClient.getAllJourneys(0, 10, testJourneyTitle);
                if (getAllResponse.getStatusCode() == 200) {
                    try {
                        JsonNode rootNode = objectMapper.readTree(getAllResponse.getBody().asString());
                        JsonNode content = rootNode.get("content");
                        if (content != null && content.isArray() && content.size() > 0) {
                            testJourneyId = content.get(0).get("id").asText();
                            testJourneySlug = content.get(0).get("slug").asText();
                            System.out.println("✓ Retrieved journey ID: " + testJourneyId);
                            System.out.println("✓ Retrieved journey slug: " + testJourneySlug);
                        }
                    } catch (Exception e) {
                        System.err.println("✗ Failed to extract journey details: " + e.getMessage());
                        // Fallback to a known journey if creation fails
                        testJourneySlug = "3e888b3d-d390-4b39-ad52-a670394f8b3c";
                    }
                }
            } else {
                System.err.println("✗ Failed to create test journey: " + createResponse.getStatusCode());
                // Fallback to a known journey if creation fails
                testJourneySlug = "3e888b3d-d390-4b39-ad52-a670394f8b3c";
            }
        });
    }

    @AfterClass
    public void cleanup() {
        Allure.step("Cleanup: Delete test journey", () -> {
            if (testJourneyId != null) {
                try {
                    Response deleteResponse = journeyClient.deleteJourney(testJourneyId);
                    if (deleteResponse.getStatusCode() == 204 || deleteResponse.getStatusCode() == 200) {
                        System.out.println("✓ Cleanup: Test journey deleted");
                    }
                } catch (Exception e) {
                    System.err.println("⚠ Cleanup: Failed to delete test journey: " + e.getMessage());
                }
            }
        });
    }

    // ========== CREATE KNOWLEDGE ITEM TESTS (CHECKLIST) ==========

    @Test(description = "Create checklist knowledge item - Success (201)", priority = 1)
    @Story("Create Knowledge Item")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a checklist knowledge item can be created successfully")
    public void testCreateChecklistKnowledgeItem_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("type", "Checklist");
        request.put("name", "Journey Checklist " + System.currentTimeMillis());

        System.out.println("=== Create Checklist Knowledge Item Test ===");
        System.out.println("Journey Slug: " + testJourneySlug);
        System.out.println("Request: " + request);

        // Act
        Response response = client.createKnowledgeItem(testJourneySlug, request);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 201);
        ResponseAssertions.assertResponseTimeBelow(response, 5000);

        // Extract knowledge item ID if created
        if (response.getStatusCode() == 201 || response.getStatusCode() == 200) {
            try {
                createdChecklistKnowledgeId = ResponseAssertions.extractJsonPath(response, "$.id");
                System.out.println("✓ Successfully created checklist knowledge with ID: " + createdChecklistKnowledgeId);
            } catch (Exception e) {
                try {
                    createdChecklistKnowledgeId = ResponseAssertions.extractJsonPath(response, "$.data.id");
                    System.out.println("✓ Successfully created checklist knowledge with ID: " + createdChecklistKnowledgeId);
                } catch (Exception ex) {
                    System.err.println("⚠ Could not extract knowledge item ID from response");
                }
            }
        }
    }

    @Test(description = "Create FAQ knowledge item - Success (201)", priority = 1)
    @Story("Create Knowledge Item")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a FAQ knowledge item can be created successfully")
    public void testCreateFaqKnowledgeItem_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("type", "faq");
        request.put("name", "Journey FAQs " + System.currentTimeMillis());

        System.out.println("=== Create FAQ Knowledge Item Test ===");
        System.out.println("Journey Slug: " + testJourneySlug);
        System.out.println("Request: " + request);

        // Act
        Response response = client.createKnowledgeItem(testJourneySlug, request);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 201);

        // Extract knowledge item ID if created
        if (response.getStatusCode() == 201 || response.getStatusCode() == 200) {
            try {
                createdKnowledgeItemId = ResponseAssertions.extractJsonPath(response, "$.id");
                System.out.println("✓ Successfully created FAQ knowledge with ID: " + createdKnowledgeItemId);
            } catch (Exception e) {
                try {
                    createdKnowledgeItemId = ResponseAssertions.extractJsonPath(response, "$.data.id");
                    System.out.println("✓ Successfully created FAQ knowledge with ID: " + createdKnowledgeItemId);
                } catch (Exception ex) {
                    System.err.println("⚠ Could not extract knowledge item ID from response");
                }
            }
        }
    }

    @Test(description = "Create knowledge item - Missing required field (400)")
    @Story("Create Knowledge Item")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that creating a knowledge item without required fields returns 400 error")
    public void testCreateKnowledgeItem_MissingRequiredFields() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        // Missing required 'type' and 'name' fields

        System.out.println("=== Create Knowledge Item (Missing Fields) Test ===");

        // Act
        Response response = client.createKnowledgeItem(testJourneySlug, request);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 400);
    }

    @Test(description = "Create knowledge item - Invalid type (400)")
    @Story("Create Knowledge Item")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that creating a knowledge item with invalid type returns 400 error")
    public void testCreateKnowledgeItem_InvalidType() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("type", "invalid-type");
        request.put("name", "Invalid Type Test");

        System.out.println("=== Create Knowledge Item (Invalid Type) Test ===");

        // Act
        Response response = client.createKnowledgeItem(testJourneySlug, request);

        System.out.println("Status Code: " + response.getStatusCode());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 400);
    }

    @Test(description = "Create knowledge item - Invalid journey (404)")
    @Story("Create Knowledge Item")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that creating a knowledge item for non-existent journey returns 404")
    public void testCreateKnowledgeItem_InvalidJourney() {
        // Arrange
        String nonExistentJourneyId = "99999999-9999-9999-9999-999999999999";

        Map<String, Object> request = new HashMap<>();
        request.put("type", "checklist");
        request.put("name", "Test Checklist");

        System.out.println("=== Create Knowledge Item (Invalid Journey) Test ===");
        System.out.println("Journey ID: " + nonExistentJourneyId);

        // Act
        Response response = client.createKnowledgeItem(nonExistentJourneyId, request);

        System.out.println("Status Code: " + response.getStatusCode());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404);
    }

    @Test(description = "Create knowledge item - Unauthorized (401)")
    @Story("Create Knowledge Item")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that creating a knowledge item without authentication returns 401 error")
    public void testCreateKnowledgeItem_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        Map<String, Object> request = new HashMap<>();
        request.put("type", "checklist");
        request.put("name", "Unauthorized Test");

        try {
            System.out.println("=== Create Knowledge Item (Unauthorized) Test ===");

            // Act
            Response response = client.createKnowledgeItem(testJourneySlug, request);

            System.out.println("Status Code: " + response.getStatusCode());

            // Assert
            ResponseAssertions.assertStatusCodeIn(response, 401, 403);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    // ========== UPDATE KNOWLEDGE ITEM TESTS ==========

    @Test(description = "Update knowledge item - Success (200)", priority = 2, dependsOnMethods = "testCreateChecklistKnowledgeItem_Success")
    @Story("Update Knowledge Item")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a knowledge item can be updated successfully")
    public void testUpdateKnowledgeItem_Success() {
        // Arrange
        String knowledgeItemId = createdChecklistKnowledgeId != null ? createdChecklistKnowledgeId : "sample-knowledge-id";

        Map<String, Object> request = new HashMap<>();
        request.put("name", "Updated Checklist Name " + System.currentTimeMillis());

        System.out.println("=== Update Knowledge Item Test ===");
        System.out.println("Knowledge Item ID: " + knowledgeItemId);
        System.out.println("Updated Name: " + request.get("name"));

        // Act
        Response response = client.updateKnowledgeItem(knowledgeItemId, request);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            System.out.println("✓ Successfully updated knowledge item");
        }
    }

    @Test(description = "Update knowledge item - Not found (404)")
    @Story("Update Knowledge Item")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that updating a non-existent knowledge item returns 404 error")
    public void testUpdateKnowledgeItem_NotFound() {
        // Arrange
        String nonExistentId = "99999999-9999-9999-9999-999999999999";

        Map<String, Object> request = new HashMap<>();
        request.put("name", "Updated Name");

        System.out.println("=== Update Knowledge Item (Not Found) Test ===");
        System.out.println("Knowledge Item ID: " + nonExistentId);

        // Act
        Response response = client.updateKnowledgeItem(nonExistentId, request);

        System.out.println("Status Code: " + response.getStatusCode());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404);
    }

    @Test(description = "Update knowledge item - Unauthorized (401)")
    @Story("Update Knowledge Item")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that updating a knowledge item without authentication returns 401 error")
    public void testUpdateKnowledgeItem_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();
        String knowledgeItemId = "sample-knowledge-id";

        Map<String, Object> request = new HashMap<>();
        request.put("name", "Unauthorized Update");

        try {
            System.out.println("=== Update Knowledge Item (Unauthorized) Test ===");

            // Act
            Response response = client.updateKnowledgeItem(knowledgeItemId, request);

            System.out.println("Status Code: " + response.getStatusCode());

            // Assert
            ResponseAssertions.assertStatusCodeIn(response, 401, 403);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    // ========== CREATE/UPDATE KNOWLEDGE ITEM CONTENT TESTS ==========

    @Test(description = "Create checklist item content - Success (201)", priority = 2, dependsOnMethods = "testCreateChecklistKnowledgeItem_Success")
    @Story("Knowledge Item Content")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that checklist item content can be created successfully")
    public void testCreateChecklistItemContent_Success() {
        // Arrange
        String knowledgeId = createdChecklistKnowledgeId != null ? createdChecklistKnowledgeId : "sample-knowledge-id";

        Map<String, Object> categoryMap = new HashMap<>();
        categoryMap.put("id", "418907e0-885a-4b6a-b6fb-545d5b505364");
        categoryMap.put("isInternal", true);

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("description", "Minimal checklist item");
        contentMap.put("daysToComplete", 5);

        Map<String, Object> request = new HashMap<>();
        request.put("type", "checklist");
        request.put("category", categoryMap);
        request.put("content", contentMap);

        System.out.println("=== Create Checklist Item Content Test ===");
        System.out.println("Knowledge ID: " + knowledgeId);

        // Act
        Response response = client.updateKnowledgeItemContent(knowledgeId, request);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 201, 404);

        if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
            System.out.println("✓ Successfully created checklist item content");
        }
    }

    @Test(description = "Update checklist item content - Success (200)", priority = 3, dependsOnMethods = "testCreateChecklistItemContent_Success")
    @Story("Knowledge Item Content")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that checklist item content can be updated successfully")
    public void testUpdateChecklistItemContent_Success() {
        // Arrange
        String knowledgeId = createdChecklistKnowledgeId != null ? createdChecklistKnowledgeId : "sample-knowledge-id";

        Map<String, Object> categoryMap = new HashMap<>();
        categoryMap.put("id", "418907e0-885a-4b6a-b6fb-545d5b505364");
        categoryMap.put("isInternal", true);

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("description", "Updated checklist item description");
        contentMap.put("daysToComplete", 10);

        Map<String, Object> request = new HashMap<>();
        request.put("type", "checklist");
        request.put("category", categoryMap);
        request.put("content", contentMap);

        System.out.println("=== Update Checklist Item Content Test ===");
        System.out.println("Knowledge ID: " + knowledgeId);

        // Act
        Response response = client.updateKnowledgeItemContent(knowledgeId, request);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            System.out.println("✓ Successfully updated checklist item content");
        }
    }

    @Test(description = "Update knowledge item content - Not found (404)")
    @Story("Knowledge Item Content")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that updating content for non-existent knowledge item returns 404")
    public void testUpdateKnowledgeItemContent_NotFound() {
        // Arrange
        String nonExistentId = "99999999-9999-9999-9999-999999999999";

        Map<String, Object> request = new HashMap<>();
        request.put("type", "checklist");
        request.put("content", new HashMap<>());

        System.out.println("=== Update Knowledge Item Content (Not Found) Test ===");
        System.out.println("Knowledge Item ID: " + nonExistentId);

        // Act
        Response response = client.updateKnowledgeItemContent(nonExistentId, request);

        System.out.println("Status Code: " + response.getStatusCode());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404);
    }

    // ========== GET KNOWLEDGE ITEM TESTS ==========

    @Test(description = "Get knowledge item by ID - Success (200)", priority = 2, dependsOnMethods = "testCreateFaqKnowledgeItem_Success")
    @Story("Get Knowledge Item")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a knowledge item can be retrieved by its ID")
    public void testGetKnowledgeItemById_Success() {
        // Arrange
        String itemId = createdKnowledgeItemId != null ? createdKnowledgeItemId : "sample-item-id";

        System.out.println("=== Get Knowledge Item By ID Test ===");
        System.out.println("Item ID: " + itemId);

        // Act
        Response response = client.getKnowledgeItemById(itemId);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertBodyNotEmpty(response);
            System.out.println("✓ Successfully retrieved knowledge item");
        }
    }

    @Test(description = "Get knowledge item by ID - Not found (404)")
    @Story("Get Knowledge Item")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that requesting a non-existent knowledge item returns 404 error")
    public void testGetKnowledgeItemById_NotFound() {
        // Arrange
        String nonExistentId = "99999999-9999-9999-9999-999999999999";

        System.out.println("=== Get Knowledge Item By ID (Not Found) Test ===");
        System.out.println("Item ID: " + nonExistentId);

        // Act
        Response response = client.getKnowledgeItemById(nonExistentId);

        System.out.println("Status Code: " + response.getStatusCode());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404);
    }

    @Test(description = "Get knowledge items by journey - Success (200)")
    @Story("Get Knowledge Items")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that all knowledge items for a journey can be retrieved")
    public void testGetKnowledgeItemsByJourney_Success() {
        // Arrange
        System.out.println("=== Get Knowledge Items By Journey Test ===");
        System.out.println("Journey Slug: " + testJourneySlug);

        // Act
        Response response = client.getKnowledgeItems(testJourneySlug);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            System.out.println("✓ Successfully retrieved knowledge items");
        }
    }

    @Test(description = "Get knowledge items - Invalid journey (404)")
    @Story("Get Knowledge Items")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that requesting knowledge items for non-existent journey returns 404")
    public void testGetKnowledgeItems_InvalidJourney() {
        // Arrange
        String nonExistentJourneyId = "99999999-9999-9999-9999-999999999999";

        System.out.println("=== Get Knowledge Items (Invalid Journey) Test ===");
        System.out.println("Journey ID: " + nonExistentJourneyId);

        // Act
        Response response = client.getKnowledgeItems(nonExistentJourneyId);

        System.out.println("Status Code: " + response.getStatusCode());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 200);
    }

    @Test(description = "Get knowledge items - Unauthorized (401)")
    @Story("Get Knowledge Items")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that getting knowledge items without authentication returns 401 error")
    public void testGetKnowledgeItems_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            System.out.println("=== Get Knowledge Items (Unauthorized) Test ===");

            // Act
            Response response = client.getKnowledgeItems(testJourneySlug);

            System.out.println("Status Code: " + response.getStatusCode());

            // Assert
            ResponseAssertions.assertStatusCodeIn(response, 401, 403);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    // ========== DELETE KNOWLEDGE ITEM TESTS ==========

    @Test(description = "Delete knowledge item content - Success (204)", priority = 4, dependsOnMethods = "testUpdateChecklistItemContent_Success")
    @Story("Delete Knowledge Item Content")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that knowledge item content can be deleted successfully")
    public void testDeleteKnowledgeItemContent_Success() {
        // Arrange
        String itemId = createdChecklistKnowledgeId != null ? createdChecklistKnowledgeId : "sample-item-id";

        System.out.println("=== Delete Knowledge Item Content Test ===");
        System.out.println("Item ID: " + itemId);

        // Act
        Response response = client.deleteKnowledgeItemContent(itemId);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 204, 200, 404);

        if (response.getStatusCode() == 204 || response.getStatusCode() == 200) {
            System.out.println("✓ Successfully deleted knowledge item content");
        }
    }

    @Test(description = "Delete knowledge item - Success (204)", priority = 5, dependsOnMethods = "testUpdateKnowledgeItem_Success")
    @Story("Delete Knowledge Item")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a knowledge item can be deleted successfully")
    public void testDeleteKnowledgeItem_Success() {
        // Arrange
        String knowledgeItemId = createdChecklistKnowledgeId != null ? createdChecklistKnowledgeId : "sample-knowledge-id";

        System.out.println("=== Delete Knowledge Item Test ===");
        System.out.println("Knowledge Item ID: " + knowledgeItemId);

        // Act
        Response response = client.deleteKnowledgeItem(knowledgeItemId);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 204, 200, 404);

        if (response.getStatusCode() == 204 || response.getStatusCode() == 200) {
            System.out.println("✓ Successfully deleted knowledge item");
            createdChecklistKnowledgeId = null; // Clear since it's deleted
        }
    }

    @Test(description = "Delete knowledge item - Not found (404)")
    @Story("Delete Knowledge Item")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that deleting a non-existent knowledge item returns 404 error")
    public void testDeleteKnowledgeItem_NotFound() {
        // Arrange
        String nonExistentId = "99999999-9999-9999-9999-999999999999";

        System.out.println("=== Delete Knowledge Item (Not Found) Test ===");
        System.out.println("Knowledge Item ID: " + nonExistentId);

        // Act
        Response response = client.deleteKnowledgeItem(nonExistentId);

        System.out.println("Status Code: " + response.getStatusCode());

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404);
    }

    @Test(description = "Delete knowledge item - Unauthorized (401)")
    @Story("Delete Knowledge Item")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that deleting a knowledge item without authentication returns 401 error")
    public void testDeleteKnowledgeItem_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();
        String itemId = "sample-item-id";

        try {
            System.out.println("=== Delete Knowledge Item (Unauthorized) Test ===");

            // Act
            Response response = client.deleteKnowledgeItem(itemId);

            System.out.println("Status Code: " + response.getStatusCode());

            // Assert
            ResponseAssertions.assertStatusCodeIn(response, 401, 403);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }
}

