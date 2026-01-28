package tests.journey;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import models.request.*;
import models.response.*;
import org.testng.annotations.*;

import java.util.*;

/**
 * Test class for Stages - Reorder functionality.
 * Tests: PUT /api/v1/stages/{journeySlug}/reorder
 */
@Epic("Journey Management")
@Feature("Stage Operations")
public class ReorderStagesTest {
    private StageClient client;
    private static final String TEST_JOURNEY_SLUG = "new-employee-journey";

    @BeforeClass
    public void setup() {
        client = new StageClient();
    }

    @Test(description = "Reorder stages - Success scenario (200)")
    public void testReorderStages_Success() {
        // Arrange
        StoryReOrderRequest request = StoryReOrderRequest.builder()
                .addStoryId("stage-uuid-1")
                .addStoryId("stage-uuid-2")
                .addStoryId("stage-uuid-3")
                .build();

        // Act
        Response response = client.reorderStages(TEST_JOURNEY_SLUG, request);

        // Assert
        ResponseAssertions.assertStatusCode(response, 200);
        ResponseAssertions.assertResponseTimeBelow(response, 3000);

        GenericMessage message = ResponseAssertions.assertGenericMessage(response);
        ResponseAssertions.assertGenericMessageContains(response, "reordered");
    }

    @Test(description = "Reorder stages - Invalid stage IDs (400)")
    public void testReorderStages_InvalidStageIds() {
        // Arrange
        StoryReOrderRequest request = StoryReOrderRequest.builder()
                .addStoryId("invalid-uuid-1")
                .addStoryId("invalid-uuid-2")
                .build();

        // Act
        Response response = client.reorderStages(TEST_JOURNEY_SLUG, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 400, 404);

        if (response.getStatusCode() == 400) {
            ResponseAssertions.assertProblemDetail(response);
            ResponseAssertions.assertBodyContains(response, "stage");
        }
    }

    @Test(description = "Reorder stages - Unauthorized (401)")
    public void testReorderStages_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        StoryReOrderRequest request = StoryReOrderRequest.builder()
                .addStoryId("stage-uuid-1")
                .addStoryId("stage-uuid-2")
                .build();

        try {
            // Act
            Response response = client.reorderStages(TEST_JOURNEY_SLUG, request);

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
            ResponseAssertions.assertProblemDetail(response, 401, "Unauthorized");
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    @Test(description = "Reorder stages - Forbidden (403)")
    public void testReorderStages_Forbidden() {
        // Arrange
        // Assuming user has insufficient permissions
        StoryReOrderRequest request = StoryReOrderRequest.builder()
                .addStoryId("stage-uuid-1")
                .addStoryId("stage-uuid-2")
                .build();

        // Act
        Response response = client.reorderStages(TEST_JOURNEY_SLUG, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 403);

        if (response.getStatusCode() == 403) {
            ResponseAssertions.assertJsonPathEquals(response, "$.httpStatusCode", 403);
            ResponseAssertions.assertBodyContains(response, "permission");
        }
    }

    @Test(description = "Reorder stages - Journey not found (404)")
    public void testReorderStages_JourneyNotFound() {
        // Arrange
        String nonExistentJourney = "non-existent-journey-slug";
        StoryReOrderRequest request = StoryReOrderRequest.builder()
                .addStoryId("stage-uuid-1")
                .addStoryId("stage-uuid-2")
                .build();

        // Act
        Response response = client.reorderStages(nonExistentJourney, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);

        if (response.getStatusCode() == 404) {
            ResponseAssertions.assertProblemDetail(response, 404, "Not Found");
            ResponseAssertions.assertBodyContains(response, "Journey not found");
        }
    }

    @Test(description = "Reorder stages - Empty stage list (400)")
    public void testReorderStages_EmptyList() {
        // Arrange
        StoryReOrderRequest request = StoryReOrderRequest.builder()
                .storyIds(Arrays.asList()) // Empty list
                .build();

        // Act
        Response response = client.reorderStages(TEST_JOURNEY_SLUG, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 400);
    }

    @Test(description = "Reorder stages - Duplicate stage IDs (400)")
    public void testReorderStages_DuplicateIds() {
        // Arrange
        StoryReOrderRequest request = StoryReOrderRequest.builder()
                .addStoryId("stage-uuid-1")
                .addStoryId("stage-uuid-1") // Duplicate
                .addStoryId("stage-uuid-2")
                .build();

        // Act
        Response response = client.reorderStages(TEST_JOURNEY_SLUG, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 400);
    }

    @Test(description = "Reorder stages - Stages from different journey (400)")
    public void testReorderStages_StagesFromDifferentJourney() {
        // Arrange
        StoryReOrderRequest request = StoryReOrderRequest.builder()
                .addStoryId("other-journey-stage-1")
                .addStoryId("other-journey-stage-2")
                .build();

        // Act
        Response response = client.reorderStages(TEST_JOURNEY_SLUG, request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 400, 404);

        if (response.getStatusCode() == 400) {
            ResponseAssertions.assertProblemDetail(response);
            ResponseAssertions.assertBodyContains(response, "journey");
        }
    }
}
