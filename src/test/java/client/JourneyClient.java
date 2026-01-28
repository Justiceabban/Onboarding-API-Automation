package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Journey endpoints.
 * Handles journey retrieval, updates, and management operations.
 */
public class JourneyClient {
    private static final String BASE_PATH = "/api/v1";

    /**
     * Get journey by ID.
     * GET /api/v1/journey/{journeyId}
     * @param journeyId Unique identifier of the journey
     * @return Response with journey details
     */
    public Response getJourneyById(String journeyId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("journeyId", journeyId)
                .when()
                .get(BASE_PATH + "/journey/{journeyId}");
    }

    /**
     * Update journey.
     * PUT /api/v1/journey/{journeyId}
     * @param journeyId Unique identifier of the journey
     * @param requestBody Journey update request object
     * @return Response with generic message
     */
    public Response updateJourney(String journeyId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("journeyId", journeyId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/journey/{journeyId}");
    }

    /**
     * Get journey settings.
     * GET /api/v1/journey/{journeyId}/settings
     * @param journeyId Unique identifier of the journey
     * @return Response with journey settings
     */
    public Response getJourneySettings(String journeyId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("journeyId", journeyId)
                .when()
                .get(BASE_PATH + "/journey/{journeyId}/settings");
    }

    /**
     * Generate preview token for journey.
     * GET /api/v1/preview/{journeyId}
     * @param journeyId Unique identifier of the journey
     * @return Response with preview token
     */
    public Response generatePreviewToken(String journeyId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("journeyId", journeyId)
                .when()
                .get(BASE_PATH + "/preview/{journeyId}");
    }
}
