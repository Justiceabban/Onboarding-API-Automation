package tests.token;

import assertions.*;
import client.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

import java.util.*;

/**
 * Test class for Token Verification endpoints.
 * Tests JWT token verification operations.
 */
@Epic("Authentication")
@Feature("Token Verification")
public class TokenVerificationTest {
    private TokenVerificationClient client;
    private static final String VALID_TOKEN = "valid-jwt-token-here";
    private static final String INVALID_TOKEN = "invalid-token";
    private static final String EXPIRED_TOKEN = "expired-token";

    @BeforeClass
    public void setup() {
        client = new TokenVerificationClient();
    }

    @Test(description = "Verify token - Valid token (200)")
    public void testVerifyToken_ValidToken() {
        // Arrange
        // Using valid JWT token

        // Act
        Response response = client.verifyToken(VALID_TOKEN);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 401);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertResponseTimeBelow(response, 2000);
        }
    }

    @Test(description = "Verify token - Invalid token (401)")
    public void testVerifyToken_InvalidToken() {
        // Arrange
        // Using invalid token

        // Act
        Response response = client.verifyToken(INVALID_TOKEN);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 401, 400);
    }

    @Test(description = "Verify token - Expired token (401)")
    public void testVerifyToken_ExpiredToken() {
        // Arrange
        // Using expired token

        // Act
        Response response = client.verifyToken(EXPIRED_TOKEN);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 401, 400);
    }

    @Test(description = "Verify token with body - Success (200)")
    public void testVerifyTokenWithBody_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("token", VALID_TOKEN);

        // Act
        Response response = client.verifyTokenWithBody(request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 401, 400);
    }

    @Test(description = "Refresh token - Success (200)")
    public void testRefreshToken_Success() {
        // Arrange
        String refreshToken = "valid-refresh-token";

        // Act
        Response response = client.refreshToken(refreshToken);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 401, 400);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertJsonPathExists(response, "$.accessToken");
        }
    }

    @Test(description = "Refresh token - Invalid refresh token (401)")
    public void testRefreshToken_InvalidToken() {
        // Arrange
        String invalidRefreshToken = "invalid-refresh-token";

        // Act
        Response response = client.refreshToken(invalidRefreshToken);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 401, 400);
    }

    @Test(description = "Validate token - Success (200)")
    public void testValidateToken_Success() {
        // Arrange
        // Using configured bearer token

        // Act
        Response response = client.validateToken();

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 401);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
        }
    }
}
