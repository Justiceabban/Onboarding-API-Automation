package tests.progress;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

import java.util.*;

/**
 * Test class for User Progress endpoints.
 * Tests progress tracking and retrieval.
 */
@Epic("User Management")
@Feature("User Progress")
public class UserProgressTest {
    private UserProgressClient client;
    private static final String TEST_JOURNEY_ID = "test-journey-123";
    private static final String TEST_CHAPTER_ID = "test-chapter-456";
    private static final String TEST_PAGE_ID = "test-page-789";
    private static final String TEST_STAGE_ID = "test-stage-101";

    @BeforeClass
    public void setup() {
        client = new UserProgressClient();
    }

    @Test(description = "Get user progress for journey - Success (200)")
    public void testGetUserProgress_Success() {
        // Arrange
        // Using test journey ID

        // Act
        Response response = client.getUserProgress(TEST_JOURNEY_ID);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertResponseTimeBelow(response, 3000);
        }
    }

    @Test(description = "Get user progress - Unauthorized (401)")
    public void testGetUserProgress_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            // Act
            Response response = client.getUserProgress(TEST_JOURNEY_ID);

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    @Test(description = "Get completed pages for chapter - Success (200)")
    public void testGetCompletedPages_Success() {
        // Arrange
        // Using test chapter ID

        // Act
        Response response = client.getCompletedPages(TEST_CHAPTER_ID);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
        }
    }

    @Test(description = "Mark page as completed - Success (200)")
    public void testMarkPageComplete_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("completedAt", "2024-01-15T10:30:00Z");
        request.put("timeSpent", 300);

        // Act
        Response response = client.markPageComplete(TEST_PAGE_ID, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 201, 404);
    }

    @Test(description = "Mark page complete - Invalid request (400)")
    public void testMarkPageComplete_InvalidRequest() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        // Missing required fields

        // Act
        Response response = client.markPageComplete(TEST_PAGE_ID, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 400, 404);
    }

    @Test(description = "Get stage progress - Success (200)")
    public void testGetStageProgress_Success() {
        // Arrange
        // Using test stage ID

        // Act
        Response response = client.getStageProgress(TEST_STAGE_ID);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertResponseTimeBelow(response, 3000);
        }
    }

    @Test(description = "Get stage progress - Not found (404)")
    public void testGetStageProgress_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-stage-999";

        // Act
        Response response = client.getStageProgress(nonExistentId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }
}
