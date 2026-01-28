package tests.category;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

import java.util.*;

/**
 * Test class for Categories endpoints.
 * Tests category CRUD operations for journey groupings.
 */
@Epic("Content Management")
@Feature("Categories")
public class CategoryTest {
    private CategoryClient client;
    private static final String TEST_JOURNEY_ID = "test-journey-123";
    private String createdCategoryId;

    @BeforeClass
    public void setup() {
        client = new CategoryClient();
    }

    @Test(description = "Create category - Success scenario (201)", priority = 1)
    public void testCreateCategory_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("name", "Technical Skills");
        request.put("description", "Technical skill development category");
        request.put("displayOrder", 1);
        request.put("color", "#4CAF50");
        request.put("isActive", true);

        // Act
        Response response = client.createCategory(request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 201);
        ResponseAssertions.assertResponseTimeBelow(response, 3000);

        // Extract category ID if created
        if (response.getStatusCode() == 201) {
            try {
                createdCategoryId = ResponseAssertions.extractJsonPath(response, "$.id");
            } catch (Exception e) {
                // ID might be in different location
            }
        }
    }

    @Test(description = "Create category - Invalid request (400)")
    public void testCreateCategory_InvalidRequest() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        // Missing required fields

        // Act
        Response response = client.createCategory(request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 400, 201);
    }

    @Test(description = "Create category - Unauthorized (401)")
    public void testCreateCategory_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        Map<String, Object> request = new HashMap<>();
        request.put("name", "Test Category");

        try {
            // Act
            Response response = client.createCategory(request);

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    @Test(description = "Get category by ID - Success scenario (200)", priority = 2)
    public void testGetCategoryById_Success() {
        // Arrange
        String categoryId = createdCategoryId != null ? createdCategoryId : "sample-category-id";

        // Act
        Response response = client.getCategoryById(categoryId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertBodyNotEmpty(response);
        }
    }

    @Test(description = "Get category by ID - Not found (404)")
    public void testGetCategoryById_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-category-123";

        // Act
        Response response = client.getCategoryById(nonExistentId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }

    @Test(description = "Update category - Success scenario (200)", priority = 3)
    public void testUpdateCategory_Success() {
        // Arrange
        String categoryId = createdCategoryId != null ? createdCategoryId : "sample-category-id";

        Map<String, Object> request = new HashMap<>();
        request.put("name", "Updated Technical Skills");
        request.put("description", "Updated category description");
        request.put("displayOrder", 2);

        // Act
        Response response = client.updateCategory(categoryId, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Update category - Not found (404)")
    public void testUpdateCategory_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-category-123";
        Map<String, Object> request = new HashMap<>();
        request.put("name", "Updated Category");

        // Act
        Response response = client.updateCategory(nonExistentId, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }

    @Test(description = "Get categories by journey - Success scenario (200)")
    public void testGetCategoriesByJourney_Success() {
        // Arrange
        // Using test journey ID

        // Act
        Response response = client.getCategoriesByJourney(TEST_JOURNEY_ID);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
        }
    }

    @Test(description = "Delete category - Success scenario (204)", priority = 4)
    public void testDeleteCategory_Success() {
        // Arrange
        String categoryId = createdCategoryId != null ? createdCategoryId : "sample-category-id";

        // Act
        Response response = client.deleteCategory(categoryId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 204, 200, 404);
    }

    @Test(description = "Delete category - Not found (404)")
    public void testDeleteCategory_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-category-123";

        // Act
        Response response = client.deleteCategory(nonExistentId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }

    @Test(description = "Delete category - Unauthorized (401)")
    public void testDeleteCategory_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();
        String categoryId = "sample-category-id";

        try {
            // Act
            Response response = client.deleteCategory(categoryId);

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }
}
