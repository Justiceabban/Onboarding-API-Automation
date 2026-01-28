package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Content Map endpoints.
 * Handles content translations and internationalization management.
 */
public class ContentMapClient {
    private static final String BASE_PATH = "/api/v1/content-maps";

    /**
     * Search content maps.
     * GET /api/v1/content-maps
     * @param searchTerm Search term for courseId, languageCode, or courseName
     * @param migrated Filter by migration status
     * @param page Page number
     * @param size Page size
     * @return Response with paginated content maps
     */
    public Response searchContentMaps(String searchTerm, Boolean migrated, int page, int size) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("searchTerm", searchTerm)
                .queryParam("migrated", migrated)
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(BASE_PATH);
    }

    /**
     * Search content maps with search term only.
     * @param searchTerm Search term
     * @return Response with content maps
     */
    public Response searchContentMaps(String searchTerm) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("searchTerm", searchTerm)
                .when()
                .get(BASE_PATH);
    }

    /**
     * Create content map.
     * POST /api/v1/content-maps
     * @param requestBody Content map creation request
     * @return Response with generic message
     */
    public Response createContentMap(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH);
    }

    /**
     * Get content map by ID.
     * GET /api/v1/content-maps/{id}
     * @param contentMapId Unique identifier of the content map
     * @return Response with content map details
     */
    public Response getContentMapById(String contentMapId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("id", contentMapId)
                .when()
                .get(BASE_PATH + "/{id}");
    }

    /**
     * Update content map.
     * PUT /api/v1/content-maps/{id}
     * @param contentMapId Unique identifier of the content map
     * @param requestBody Content map update request
     * @return Response with generic message
     */
    public Response updateContentMap(String contentMapId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("id", contentMapId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{id}");
    }

    /**
     * Delete content map.
     * DELETE /api/v1/content-maps/{id}
     * @param contentMapId Unique identifier of the content map
     * @return Response
     */
    public Response deleteContentMap(String contentMapId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("id", contentMapId)
                .when()
                .delete(BASE_PATH + "/{id}");
    }
}
