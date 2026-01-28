package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Journey Knowledge Items endpoints.
 * Handles knowledge item management and content operations.
 */
public class KnowledgeItemClient {
    private static final String BASE_PATH = "/api/v1/journeys";

    /**
     * Get knowledge items for a journey.
     * GET /api/v1/journeys/{journeyId}/knowledge-items
     * @param journeyId Unique identifier of the journey
     * @return Response with knowledge items list
     */
    public Response getKnowledgeItems(String journeyId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("journeyId", journeyId)
                .when()
                .get(BASE_PATH + "/{journeyId}/knowledge-items");
    }

    /**
     * Create knowledge item for a journey.
     * POST /api/v1/journeys/{journeyId}/knowledge-items
     * @param journeyId Unique identifier of the journey
     * @param requestBody Knowledge item creation request
     * @return Response with created knowledge item
     */
    public Response createKnowledgeItem(String journeyId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("journeyId", journeyId)
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/{journeyId}/knowledge-items");
    }

    /**
     * Update knowledge item.
     * PUT /api/v1/journeys/{knowledgeItemId}/knowledge-items
     * @param knowledgeItemId Unique identifier of the knowledge item
     * @param requestBody Knowledge item update request
     * @return Response with updated knowledge item
     */
    public Response updateKnowledgeItem(String knowledgeItemId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("knowledgeItemId", knowledgeItemId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{knowledgeItemId}/knowledge-items");
    }

    /**
     * Delete knowledge item.
     * DELETE /api/v1/journeys/{knowledgeItemId}/knowledge-items
     * @param knowledgeItemId Unique identifier of the knowledge item
     * @return Response
     */
    public Response deleteKnowledgeItem(String knowledgeItemId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("knowledgeItemId", knowledgeItemId)
                .when()
                .delete(BASE_PATH + "/{knowledgeItemId}/knowledge-items");
    }

    /**
     * Update knowledge item content.
     * PUT /api/v1/journeys/knowledge-items/{itemId}
     * @param itemId Unique identifier of the knowledge item
     * @param requestBody Knowledge item content request
     * @return Response with generic message
     */
    public Response updateKnowledgeItemContent(String itemId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("itemId", itemId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/knowledge-items/{itemId}");
    }

    /**
     * Delete knowledge item content.
     * DELETE /api/v1/journeys/knowledge-items/{itemId}
     * @param itemId Unique identifier of the knowledge item
     * @return Response
     */
    public Response deleteKnowledgeItemContent(String itemId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("itemId", itemId)
                .when()
                .delete(BASE_PATH + "/knowledge-items/{itemId}");
    }

    /**
     * Get knowledge item by ID.
     * GET /api/v1/journeys/knowledge-items/{itemId}
     * @param itemId Unique identifier of the knowledge item
     * @return Response with knowledge item details
     */
    public Response getKnowledgeItemById(String itemId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("itemId", itemId)
                .when()
                .get(BASE_PATH + "/knowledge-items/{itemId}");
    }
}
