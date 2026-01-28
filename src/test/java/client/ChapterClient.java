package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Chapter Management endpoints.
 * Handles chapter story management operations.
 */
public class ChapterClient {
    private static final String BASE_PATH = "/api/v1/chapters";

    /**
     * Get chapter by ID.
     * GET /api/v1/chapters/{chapterId}
     * @param chapterId Unique identifier of the chapter
     * @return Response with chapter details
     */
    public Response getChapterById(String chapterId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("chapterId", chapterId)
                .when()
                .get(BASE_PATH + "/{chapterId}");
    }

    /**
     * Create a new chapter.
     * POST /api/v1/chapters
     * @param requestBody Chapter creation request
     * @return Response with created chapter
     */
    public Response createChapter(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH);
    }

    /**
     * Update chapter.
     * PUT /api/v1/chapters/{chapterId}
     * @param chapterId Unique identifier of the chapter
     * @param requestBody Chapter update request
     * @return Response with generic message
     */
    public Response updateChapter(String chapterId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("chapterId", chapterId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{chapterId}");
    }

    /**
     * Delete chapter.
     * DELETE /api/v1/chapters/{chapterId}
     * @param chapterId Unique identifier of the chapter
     * @return Response
     */
    public Response deleteChapter(String chapterId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("chapterId", chapterId)
                .when()
                .delete(BASE_PATH + "/{chapterId}");
    }

    /**
     * Get chapters for a stage.
     * GET /api/v1/chapters/stage/{stageId}
     * @param stageId Unique identifier of the stage
     * @return Response with list of chapters
     */
    public Response getChaptersByStage(String stageId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("stageId", stageId)
                .when()
                .get(BASE_PATH + "/stage/{stageId}");
    }

    /**
     * Update chapter tags.
     * PUT /api/v1/chapters/{chapterId}/tags
     * @param chapterId Unique identifier of the chapter
     * @param requestBody Tags request
     * @return Response with generic message
     */
    public Response updateChapterTags(String chapterId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("chapterId", chapterId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{chapterId}/tags");
    }

    /**
     * Remove chapter tags.
     * DELETE /api/v1/chapters/{chapterId}/tags
     * @param chapterId Unique identifier of the chapter
     * @param requestBody Tags request
     * @return Response with generic message
     */
    public Response removeChapterTags(String chapterId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("chapterId", chapterId)
                .body(requestBody)
                .when()
                .delete(BASE_PATH + "/{chapterId}/tags");
    }
}
