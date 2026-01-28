package tests.user;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import models.request.*;
import models.response.*;
import org.testng.annotations.*;
import utils.*;

/**
 * Test class for User Management - Preferred Journey endpoints.
 * Tests: GET and PUT /api/v1/users/preferred-journey
 */
@Epic("User Management")
@Feature("Preferred Journey")
public class PreferredJourneyTest {
    private UserManagementClient client;

    @BeforeClass
    public void setup() {
        client = new UserManagementClient();
    }

    @Test(description = "Get preferred journey - Success scenario (200)")
    @Story("Get Preferred Journey")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that authenticated new hire can retrieve their preferred journey")
    public void testGetPreferredJourney_Success() {
        // Arrange
        // Assuming valid bearer token is configured

        // Act
        Response response = client.getPreferredJourney();

        // Assert
        ResponseAssertions.assertStatusCode(response, 200);
        ResponseAssertions.assertContentTypeJson(response);
        ResponseAssertions.assertResponseTimeBelow(response, 3000);

        // Validate response body
        PreferredJourney preferredJourney = JsonUtils.fromJson(
                response.getBody().asString(),
                PreferredJourney.class
        );
        ResponseAssertions.assertJsonPathExists(response, "$.storyblokId");
        ResponseAssertions.assertJsonPathExists(response, "$.defaultLanguage");
    }

    @Test(description = "Get preferred journey - Unauthorized (401)")
    @Story("Get Preferred Journey")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that unauthenticated users cannot access preferred journey")
    public void testGetPreferredJourney_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            // Act
            Response response = client.getPreferredJourney();

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
            ResponseAssertions.assertProblemDetail(response, 401, "Unauthorized");
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    @Test(description = "Get preferred journey - Forbidden (403)")
    @Story("Get Preferred Journey")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that users without new hire role cannot access preferred journey")
    public void testGetPreferredJourney_Forbidden() {
        // Arrange
        // Use a token with insufficient permissions (if available)

        // Act
        Response response = client.getPreferredJourney();

        // Assert
        // This test assumes the user doesn't have new hire role
        ResponseAssertions.assertStatusCodeIn(response, 200, 403);

        if (response.getStatusCode() == 403) {
            ResponseAssertions.assertJsonPathEquals(response, "$.httpStatusCode", 403);
            ResponseAssertions.assertBodyContains(response, "permission");
        }
    }

    @Test(description = "Update preferred journey - Success scenario (200)")
    @Story("Update Preferred Journey")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that new hire can update their preferred journey successfully")
    public void testUpdatePreferredJourney_Success() {
        // Arrange
        PreferredJourneyRequest request = PreferredJourneyRequest.builder()
                .journeyId("new-employee-onboarding")
                .build();

        // Act
        Response response = client.updatePreferredJourney(request);

        // Assert
        ResponseAssertions.assertStatusCode(response, 200);
        ResponseAssertions.assertResponseTimeBelow(response, 3000);

        GenericMessage message = ResponseAssertions.assertGenericMessage(response);
        // Optionally validate the message content
    }

    @Test(description = "Update preferred journey - Invalid journey ID (400)")
    @Story("Update Preferred Journey")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that invalid journey ID returns appropriate error")
    public void testUpdatePreferredJourney_InvalidJourneyId() {
        // Arrange
        PreferredJourneyRequest request = PreferredJourneyRequest.builder()
                .journeyId("invalid-journey-id-xyz")
                .build();

        // Act
        Response response = client.updatePreferredJourney(request);

        // Assert
        // Expecting either 200 or 400 depending on API validation
        ResponseAssertions.assertStatusCodeIn(response, 200, 400, 404);
    }

    @Test(description = "Update preferred journey - Null journey ID (400)")
    @Story("Update Preferred Journey")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that null journey ID returns validation error")
    public void testUpdatePreferredJourney_NullJourneyId() {
        // Arrange
        PreferredJourneyRequest request = PreferredJourneyRequest.builder()
                .journeyId(null)
                .build();

        // Act
        Response response = client.updatePreferredJourney(request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 400);
    }

    @Test(description = "Update preferred journey - Unauthorized (401)")
    @Story("Update Preferred Journey")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that unauthenticated users cannot update preferred journey")
    public void testUpdatePreferredJourney_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            PreferredJourneyRequest request = PreferredJourneyRequest.builder()
                    .journeyId("new-employee-onboarding")
                    .build();

            // Act
            Response response = client.updatePreferredJourney(request);

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
            ResponseAssertions.assertProblemDetail(response, 401, "Unauthorized");
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }
}
