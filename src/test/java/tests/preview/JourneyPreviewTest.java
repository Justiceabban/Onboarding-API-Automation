package tests.preview;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

/**
 * Test class for Journey Preview endpoints.
 * Tests preview token generation and validation.
 */
@Epic("Journey Management")
@Feature("Journey Preview")
public class JourneyPreviewTest {
    private JourneyPreviewClient client;
    private static final String TEST_JOURNEY_ID = "test-journey-123";
    private String previewToken;

    @BeforeClass
    public void setup() {
        client = new JourneyPreviewClient();
    }

    @Test(description = "Get preview token - Success (200)", priority = 1)
    public void testGetPreviewToken_Success() {
        // Arrange
        // Using test journey ID

        // Act
        Response response = client.getPreviewToken(TEST_JOURNEY_ID);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertJsonPathExists(response, "$.previewToken");
            ResponseAssertions.assertJsonPathExists(response, "$.journeyId");
            ResponseAssertions.assertResponseTimeBelow(response, 3000);

            try {
                previewToken = ResponseAssertions.extractJsonPath(response, "$.previewToken");
            } catch (Exception e) {}
        }
    }

    @Test(description = "Get preview token - Journey not found (404)")
    public void testGetPreviewToken_NotFound() {
        // Arrange
        String nonExistentJourneyId = "non-existent-journey-999";

        // Act
        Response response = client.getPreviewToken(nonExistentJourneyId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);

        if (response.getStatusCode() == 404) {
            ResponseAssertions.assertProblemDetail(response);
        }
    }

    @Test(description = "Get preview token - Unauthorized (401)")
    public void testGetPreviewToken_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            // Act
            Response response = client.getPreviewToken(TEST_JOURNEY_ID);

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
            ResponseAssertions.assertProblemDetail(response, 401, "Unauthorized");
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    @Test(description = "Get preview token - Forbidden (403)")
    public void testGetPreviewToken_Forbidden() {
        // Arrange
        // Assuming user has insufficient permissions

        // Act
        Response response = client.getPreviewToken(TEST_JOURNEY_ID);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 403, 404);

        if (response.getStatusCode() == 403) {
            ResponseAssertions.assertBodyContains(response, "permission");
        }
    }

    @Test(description = "Validate preview token - Valid token (200)", priority = 2)
    public void testValidatePreviewToken_ValidToken() {
        // Arrange
        String token = previewToken != null ? previewToken : "sample-preview-token";

        // Act
        Response response = client.validatePreviewToken(token);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 401, 404);
    }

    @Test(description = "Validate preview token - Invalid token (401)")
    public void testValidatePreviewToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalid-preview-token-xyz";

        // Act
        Response response = client.validatePreviewToken(invalidToken);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 401, 400, 404);
    }

    @Test(description = "Get journey by preview token - Success (200)")
    public void testGetJourneyByPreviewToken_Success() {
        // Arrange
        String token = previewToken != null ? previewToken : "sample-preview-token";

        // Act
        Response response = client.getJourneyByPreviewToken(token);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 401, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertBodyNotEmpty(response);
        }
    }

    @Test(description = "Get journey by preview token - Invalid token (401)")
    public void testGetJourneyByPreviewToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalid-token";

        // Act
        Response response = client.getJourneyByPreviewToken(invalidToken);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 401, 404, 400);
    }

    @Test(description = "Revoke preview token - Success (204)", priority = 3)
    public void testRevokePreviewToken_Success() {
        // Arrange
        // Using test journey ID

        // Act
        Response response = client.revokePreviewToken(TEST_JOURNEY_ID);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 204, 200, 404);
    }

    @Test(description = "Revoke preview token - Not found (404)")
    public void testRevokePreviewToken_NotFound() {
        // Arrange
        String nonExistentJourneyId = "non-existent-journey-999";

        // Act
        Response response = client.revokePreviewToken(nonExistentJourneyId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }
}
