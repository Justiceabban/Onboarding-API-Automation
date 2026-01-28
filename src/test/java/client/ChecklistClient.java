package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for User Checklists endpoints.
 * Handles user checklist retrieval and updates.
 */
public class ChecklistClient {
    private static final String BASE_PATH = "/api/v1/checklists";

    /**
     * Get user checklist.
     * GET /api/v1/checklists
     * @return Response with user checklist
     */
    public Response getChecklist() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .get(BASE_PATH);
    }

    /**
     * Update user checklist.
     * POST /api/v1/checklists
     * @param requestBody UpdateChecklistRequest object
     * @return Response with generic message
     */
    public Response updateChecklist(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH);
    }
}
