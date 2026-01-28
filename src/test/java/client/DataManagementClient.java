package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Data Management endpoints (DX system).
 * Handles data record CRUD operations.
 */
public class DataManagementClient {
    private static final String BASE_PATH = "/api/v1/data";

    /**
     * Create a data record.
     * POST /api/v1/data
     * @param requestBody Data creation request object
     * @return Response with created data record
     */
    public Response createDataRecord(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH);
    }

    /**
     * Update a data record.
     * PUT /api/v1/data/{recordId}
     * @param recordId Unique identifier of the data record
     * @param requestBody Data update request object
     * @return Response with generic message
     */
    public Response updateDataRecord(String recordId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("recordId", recordId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{recordId}");
    }

    /**
     * Delete a data record.
     * DELETE /api/v1/data/{recordId}
     * @param recordId Unique identifier of the data record
     * @return Response
     */
    public Response deleteDataRecord(String recordId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("recordId", recordId)
                .when()
                .delete(BASE_PATH + "/{recordId}");
    }

    /**
     * Get a data record by ID.
     * GET /api/v1/data/{recordId}
     * @param recordId Unique identifier of the data record
     * @return Response with data record details
     */
    public Response getDataRecordById(String recordId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("recordId", recordId)
                .when()
                .get(BASE_PATH + "/{recordId}");
    }

    /**
     * Get all data records with pagination.
     * GET /api/v1/data
     * @param page Page number
     * @param size Page size
     * @return Response with paginated data records
     */
    public Response getAllDataRecords(int page, int size) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(BASE_PATH);
    }

    /**
     * Get all data records with default pagination.
     * @return Response with paginated data records
     */
    public Response getAllDataRecords() {
        return getAllDataRecords(0, 20);
    }
}
