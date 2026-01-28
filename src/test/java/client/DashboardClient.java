package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Dashboard endpoints.
 * Handles dashboard statistics and analytics retrieval.
 */
public class DashboardClient {
    private static final String BASE_PATH = "/api/v1/dashboard";

    /**
     * Get user statistics for dashboard.
     * GET /api/v1/dashboard/users
     * @param page Page number
     * @param size Page size
     * @return Response with paginated user statistics
     */
    public Response getUserStats(int page, int size) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(BASE_PATH + "/users");
    }

    /**
     * Get user statistics with default pagination.
     * @return Response with user statistics
     */
    public Response getUserStats() {
        return getUserStats(0, 100);
    }

    /**
     * Get stage statistics for dashboard.
     * GET /api/v1/dashboard/stages
     * @param page Page number
     * @param size Page size
     * @return Response with paginated stage statistics
     */
    public Response getStageStats(int page, int size) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(BASE_PATH + "/stages");
    }

    /**
     * Get stage statistics with default pagination.
     * @return Response with stage statistics
     */
    public Response getStageStats() {
        return getStageStats(0, 100);
    }

    /**
     * Get journey statistics for dashboard.
     * GET /api/v1/dashboard/journeys
     * @param page Page number
     * @param size Page size
     * @return Response with paginated journey statistics
     */
    public Response getJourneyStats(int page, int size) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(BASE_PATH + "/journeys");
    }

    /**
     * Get journey statistics with default pagination.
     * @return Response with journey statistics
     */
    public Response getJourneyStats() {
        return getJourneyStats(0, 100);
    }

    /**
     * Get journey statistics by journey ID.
     * GET /api/v1/dashboard/journeys/{journeyId}
     * @param journeyId Unique identifier of the journey
     * @return Response with journey statistics
     */
    public Response getJourneyStatsById(String journeyId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("journeyId", journeyId)
                .when()
                .get(BASE_PATH + "/journeys/{journeyId}");
    }

    /**
     * Get overall dashboard summary.
     * GET /api/v1/dashboard/summary
     * @return Response with dashboard summary
     */
    public Response getDashboardSummary() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .get(BASE_PATH + "/summary");
    }
}
