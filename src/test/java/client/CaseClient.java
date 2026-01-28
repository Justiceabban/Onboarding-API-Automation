package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Cases endpoints.
 * Handles case management, actions, and attachments.
 */
public class CaseClient {
    private static final String BASE_PATH = "/api/v1/cases";

    /**
     * Get all cases.
     * GET /api/v1/cases
     * @param page Page number
     * @param size Page size
     * @return Response with cases list
     */
    public Response getAllCases(int page, int size) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(BASE_PATH);
    }

    /**
     * Get all cases with default pagination.
     * @return Response with cases list
     */
    public Response getAllCases() {
        return getAllCases(0, 20);
    }

    /**
     * Get case by ID.
     * GET /api/v1/cases/{caseId}
     * @param caseId Unique identifier of the case
     * @return Response with case details
     */
    public Response getCaseById(String caseId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("caseId", caseId)
                .when()
                .get(BASE_PATH + "/{caseId}");
    }

    /**
     * Create a new case.
     * POST /api/v1/cases
     * @param requestBody Case creation request
     * @return Response with created case
     */
    public Response createCase(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH);
    }

    /**
     * Update case.
     * PUT /api/v1/cases/{caseId}
     * @param caseId Unique identifier of the case
     * @param requestBody Case update request
     * @return Response with generic message
     */
    public Response updateCase(String caseId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("caseId", caseId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{caseId}");
    }

    /**
     * Delete case.
     * DELETE /api/v1/cases/{caseId}
     * @param caseId Unique identifier of the case
     * @return Response
     */
    public Response deleteCase(String caseId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("caseId", caseId)
                .when()
                .delete(BASE_PATH + "/{caseId}");
    }

    /**
     * Add case action.
     * POST /api/v1/cases/{caseId}/actions
     * @param caseId Unique identifier of the case
     * @param requestBody Case action request
     * @return Response with generic message
     */
    public Response addCaseAction(String caseId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("caseId", caseId)
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/{caseId}/actions");
    }

    /**
     * Get case attachments.
     * GET /api/v1/cases/{caseId}/attachments
     * @param caseId Unique identifier of the case
     * @return Response with case attachments
     */
    public Response getCaseAttachments(String caseId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("caseId", caseId)
                .when()
                .get(BASE_PATH + "/{caseId}/attachments");
    }
}
