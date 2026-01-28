package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Assignments endpoints.
 * Handles workflow assignments and case processing.
 */
public class AssignmentClient {
    private static final String BASE_PATH = "/api/v1/assignments";

    /**
     * Get all assignments.
     * GET /api/v1/assignments
     * @param page Page number
     * @param size Page size
     * @return Response with assignments list
     */
    public Response getAllAssignments(int page, int size) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(BASE_PATH);
    }

    /**
     * Get all assignments with default pagination.
     * @return Response with assignments list
     */
    public Response getAllAssignments() {
        return getAllAssignments(0, 20);
    }

    /**
     * Get assignment by ID.
     * GET /api/v1/assignments/{assignmentId}
     * @param assignmentId Unique identifier of the assignment
     * @return Response with assignment details
     */
    public Response getAssignmentById(String assignmentId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("assignmentId", assignmentId)
                .when()
                .get(BASE_PATH + "/{assignmentId}");
    }

    /**
     * Create a new assignment.
     * POST /api/v1/assignments
     * @param requestBody Assignment creation request
     * @return Response with created assignment
     */
    public Response createAssignment(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH);
    }

    /**
     * Update assignment.
     * PUT /api/v1/assignments/{assignmentId}
     * @param assignmentId Unique identifier of the assignment
     * @param requestBody Assignment update request
     * @return Response with generic message
     */
    public Response updateAssignment(String assignmentId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("assignmentId", assignmentId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{assignmentId}");
    }

    /**
     * Delete assignment.
     * DELETE /api/v1/assignments/{assignmentId}
     * @param assignmentId Unique identifier of the assignment
     * @return Response
     */
    public Response deleteAssignment(String assignmentId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("assignmentId", assignmentId)
                .when()
                .delete(BASE_PATH + "/{assignmentId}");
    }

    /**
     * Get assignments by user.
     * GET /api/v1/assignments/user/{userId}
     * @param userId Unique identifier of the user
     * @return Response with user's assignments
     */
    public Response getAssignmentsByUser(String userId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("userId", userId)
                .when()
                .get(BASE_PATH + "/user/{userId}");
    }
}
