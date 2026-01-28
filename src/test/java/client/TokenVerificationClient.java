package client;

import config.*;
import io.restassured.response.*;

import java.util.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Token Verification endpoints.
 * Handles JWT token verification operations.
 */
public class TokenVerificationClient {
    private static final String BASE_PATH = "/api/v1/token";

    /**
     * Verify JWT token.
     * POST /api/v1/token/verify
     * @param token JWT token to verify
     * @return Response with verification result
     */
    public Response verifyToken(String token) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .header("Authorization", "Bearer " + token)
                .when()
                .post(BASE_PATH + "/verify");
    }

    /**
     * Verify token with request body.
     * POST /api/v1/token/verify
     * @param requestBody Token verification request
     * @return Response with verification result
     */
    public Response verifyTokenWithBody(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/verify");
    }

    /**
     * Refresh token.
     * POST /api/v1/token/refresh
     * @param refreshToken Refresh token
     * @return Response with new access token
     */
    public Response refreshToken(String refreshToken) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(Map.of("refreshToken", refreshToken))
                .when()
                .post(BASE_PATH + "/refresh");
    }

    /**
     * Validate token expiry.
     * GET /api/v1/token/validate
     * @return Response with token validation status
     */
    public Response validateToken() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .get(BASE_PATH + "/validate");
    }
}
