package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for User Progress endpoints.
 * Handles user progress tracking and retrieval.
 */
public class UserProgressClient {
    private static final String BASE_PATH = "/api/v1/progress";

    /**
     * Get user progress for a journey.
     * GET /api/v1/progress/journey/{journeyId}
     * @param journeyId Unique identifier of the journey
     * @return Response with user progress
     */
    public Response getUserProgress(String journeyId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("journeyId", journeyId)
                .when()
                .get(BASE_PATH + "/journey/{journeyId}");
    }

    /**
     * Get completed pages for a chapter.
     * GET /api/v1/progress/chapter/{chapterId}
     * @param chapterId Unique identifier of the chapter
     * @return Response with completed pages
     */
    public Response getCompletedPages(String chapterId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("chapterId", chapterId)
                .when()
                .get(BASE_PATH + "/chapter/{chapterId}");
    }

    /**
     * Mark page as completed.
     * POST /api/v1/progress/page/{pageId}/complete
     * @param pageId Unique identifier of the page
     * @param requestBody Completion request body
     * @return Response with generic message
     */
    public Response markPageComplete(String pageId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("pageId", pageId)
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/page/{pageId}/complete");
    }

    /**
     * Get stage chapter progress.
     * GET /api/v1/progress/stage/{stageId}
     * @param stageId Unique identifier of the stage
     * @return Response with stage progress
     */
    public Response getStageProgress(String stageId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("stageId", stageId)
                .when()
                .get(BASE_PATH + "/stage/{stageId}");
    }
}
