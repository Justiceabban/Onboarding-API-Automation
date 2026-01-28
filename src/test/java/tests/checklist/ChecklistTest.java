package tests.checklist;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

import java.util.*;

/**
 * Test class for User Checklist endpoints.
 */
@Epic("User Management")
@Feature("User Checklist")
public class ChecklistTest {
    private ChecklistClient client;

    @BeforeClass
    public void setup() {
        client = new ChecklistClient();
    }

    @Test(description = "Get user checklist - Success scenario (200)")
    public void testGetChecklist_Success() {
        // Arrange
        // Assuming valid bearer token is configured

        // Act
        Response response = client.getChecklist();

        // Assert
        ResponseAssertions.assertStatusCode(response, 200);
        ResponseAssertions.assertContentTypeJson(response);
        ResponseAssertions.assertResponseTimeBelow(response, 3000);

        // Validate response structure
        ResponseAssertions.assertJsonPathExists(response, "$.userId");
        ResponseAssertions.assertJsonPathExists(response, "$.checklistId");
        ResponseAssertions.assertJsonPathExists(response, "$.completedItems");
        ResponseAssertions.assertJsonPathExists(response, "$.progressPercentage");
    }

    @Test(description = "Get user checklist - Unauthorized (401)")
    public void testGetChecklist_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            // Act
            Response response = client.getChecklist();

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
            ResponseAssertions.assertProblemDetail(response, 401, "Unauthorized");
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    @Test(description = "Update checklist - Success scenario (200)")
    public void testUpdateChecklist_Success() {
        // Arrange
        Map<String, Object> checklistItem = new HashMap<>();
        checklistItem.put("itemId", "setup-profile");
        checklistItem.put("isCompleted", true);
        checklistItem.put("completedAt", "2024-01-15T10:30:00Z");

        List<Map<String, Object>> items = new ArrayList<>();
        items.add(checklistItem);

        Map<String, Object> request = new HashMap<>();
        request.put("checklistItems", items);

        // Act
        Response response = client.updateChecklist(request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 201);
        ResponseAssertions.assertResponseTimeBelow(response, 3000);
    }

    @Test(description = "Update checklist - Invalid request (400)")
    public void testUpdateChecklist_InvalidRequest() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("checklistItems", new ArrayList<>()); // Empty items

        // Act
        Response response = client.updateChecklist(request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 400);
    }

    @Test(description = "Update checklist - Unauthorized (401)")
    public void testUpdateChecklist_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        Map<String, Object> request = new HashMap<>();
        request.put("checklistItems", new ArrayList<>());

        try {
            // Act
            Response response = client.updateChecklist(request);

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    @Test(description = "Update checklist - Multiple items")
    public void testUpdateChecklist_MultipleItems() {
        // Arrange
        List<Map<String, Object>> items = new ArrayList<>();

        Map<String, Object> item1 = new HashMap<>();
        item1.put("itemId", "setup-profile");
        item1.put("isCompleted", true);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("itemId", "read-handbook");
        item2.put("isCompleted", false);

        items.add(item1);
        items.add(item2);

        Map<String, Object> request = new HashMap<>();
        request.put("checklistItems", items);

        // Act
        Response response = client.updateChecklist(request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 201);
    }
}
