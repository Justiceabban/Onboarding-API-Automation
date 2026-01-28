package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Journey Preview endpoints.
 * Handles preview token generation for journeys.
 */
public class JourneyPreviewClient {
    private static final String BASE_PATH = "/api/v1/preview";

    /**
     * Get journey preview token.
     * GET /api/v1/preview/{journeyId}
     * @param journeyId Unique identifier of the journey
     * @return Response with preview token
     */
    public Response getPreviewToken(String journeyId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("journeyId", journeyId)
                .when()
                .get(BASE_PATH + "/{journeyId}");
    }

    /**
     * Validate preview token.
     * POST /api/v1/preview/validate
     * @param token Preview token to validate
     * @return Response with validation result
     */
    public Response validatePreviewToken(String token) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("token", token)
                .when()
                .post(BASE_PATH + "/validate");
    }

    /**
     * Get journey by preview token.
     * GET /api/v1/preview/journey
     * @param token Preview token
     * @return Response with journey details
     */
    public Response getJourneyByPreviewToken(String token) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("token", token)
                .when()
                .get(BASE_PATH + "/journey");
    }

    /**
     * Revoke preview token.
     * DELETE /api/v1/preview/{journeyId}
     * @param journeyId Unique identifier of the journey
     * @return Response
     */
    public Response revokePreviewToken(String journeyId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("journeyId", journeyId)
                .when()
                .delete(BASE_PATH + "/{journeyId}");
    }
}
