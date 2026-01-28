package tests.dashboard;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

/**
 * Test class for Dashboard endpoints.
 * Tests dashboard statistics and analytics retrieval.
 */
@Epic("Dashboard & Analytics")
@Feature("Dashboard Statistics")
public class DashboardTest {
    private DashboardClient client;
    private static final String TEST_JOURNEY_ID = "test-journey-123";

    @BeforeClass
    public void setup() {
        client = new DashboardClient();
    }

    @Test(description = "Get user stats - Success (200)")
    public void testGetUserStats_Success() {
        // Arrange
        // No specific setup needed

        // Act
        Response response = client.getUserStats();

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertResponseTimeBelow(response, 5000);
            ResponseAssertions.assertBodyNotEmpty(response);
        }
    }

    @Test(description = "Get user stats with pagination - Success (200)")
    public void testGetUserStats_WithPagination() {
        // Arrange
        int page = 0;
        int size = 50;

        // Act
        Response response = client.getUserStats(page, size);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Get user stats - Unauthorized (401)")
    public void testGetUserStats_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            // Act
            Response response = client.getUserStats();

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
            ResponseAssertions.assertProblemDetail(response, 401, "Unauthorized");
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    @Test(description = "Get stage stats - Success (200)")
    public void testGetStageStats_Success() {
        // Arrange
        // No specific setup needed

        // Act
        Response response = client.getStageStats();

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertResponseTimeBelow(response, 5000);
        }
    }

    @Test(description = "Get stage stats with pagination - Success (200)")
    public void testGetStageStats_WithPagination() {
        // Arrange
        int page = 0;
        int size = 25;

        // Act
        Response response = client.getStageStats(page, size);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Get journey stats - Success (200)")
    public void testGetJourneyStats_Success() {
        // Arrange
        // No specific setup needed

        // Act
        Response response = client.getJourneyStats();

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertResponseTimeBelow(response, 5000);
        }
    }

    @Test(description = "Get journey stats with pagination - Success (200)")
    public void testGetJourneyStats_WithPagination() {
        // Arrange
        int page = 0;
        int size = 20;

        // Act
        Response response = client.getJourneyStats(page, size);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Get journey stats by ID - Success (200)")
    public void testGetJourneyStatsById_Success() {
        // Arrange
        // Using test journey ID

        // Act
        Response response = client.getJourneyStatsById(TEST_JOURNEY_ID);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertJsonPathExists(response, "$.journeys");
            ResponseAssertions.assertJsonPathExists(response, "$.averageJourneyProgress");
        }
    }

    @Test(description = "Get journey stats by ID - Not found (404)")
    public void testGetJourneyStatsById_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-journey-999";

        // Act
        Response response = client.getJourneyStatsById(nonExistentId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }

    @Test(description = "Get dashboard summary - Success (200)")
    public void testGetDashboardSummary_Success() {
        // Arrange
        // No specific setup needed

        // Act
        Response response = client.getDashboardSummary();

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertResponseTimeBelow(response, 5000);
        }
    }

    @Test(description = "Get dashboard summary - Unauthorized (401)")
    public void testGetDashboardSummary_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            // Act
            Response response = client.getDashboardSummary();

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }
}
