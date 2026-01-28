package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Stages endpoints.
 * Handles stage management, updates, and reordering operations.
 */
public class StageClient {
    private static final String BASE_PATH = "/api/v1/stages";

    /**
     * Update a stage.
     * PUT /api/v1/stages/{stageId}
     * @param stageId Unique identifier of the stage
     * @param requestBody StageChapterRequest object
     * @return Response with generic message
     */
    public Response updateStage(String stageId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("stageId", stageId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{stageId}");
    }

    /**
     * Reorder stages in a journey.
     * PUT /api/v1/stages/{journeySlug}/reorder
     * @param journeySlug Journey slug identifier
     * @param requestBody StoryReOrderRequest with list of stage IDs in desired order
     * @return Response with generic message
     */
    public Response reorderStages(String journeySlug, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("journeySlug", journeySlug)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{journeySlug}/reorder");
    }

    /**
     * Update stage tags.
     * PUT /api/v1/stages/{stageId}/tags
     * @param stageId Unique identifier of the stage
     * @param requestBody StoryTagRequest with tag IDs
     * @return Response with generic message
     */
    public Response updateStageTags(String stageId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("stageId", stageId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{stageId}/tags");
    }

    /**
     * Remove stage tags.
     * DELETE /api/v1/stages/{stageId}/tags
     * @param stageId Unique identifier of the stage
     * @param requestBody StoryTagRequest with tag IDs to remove
     * @return Response with generic message
     */
    public Response removeStageTags(String stageId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("stageId", stageId)
                .body(requestBody)
                .when()
                .delete(BASE_PATH + "/{stageId}/tags");
    }

    /**
     * Delete a stage.
     * DELETE /api/v1/stages/delete/{stageId}
     * @param stageId Unique identifier of the stage
     * @return Response
     */
    public Response deleteStage(String stageId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("stageId", stageId)
                .when()
                .delete(BASE_PATH + "/delete/{stageId}");
    }
}
