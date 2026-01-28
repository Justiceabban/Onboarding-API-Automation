package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Data Migration endpoints.
 * Handles legacy data migration and course content migration.
 */
public class DataMigrationClient {
    private static final String BASE_PATH = "/api/v1/migration";

    /**
     * Migrate legacy data.
     * POST /api/v1/migration/legacy
     * @param requestBody Legacy data migration request
     * @return Response with migration status
     */
    public Response migrateLegacyData(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/legacy");
    }

    /**
     * Migrate course content.
     * POST /api/v1/migration/courses
     * @param requestBody Course migration request
     * @return Response with migration status
     */
    public Response migrateCourseContent(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/courses");
    }

    /**
     * Get migration status.
     * GET /api/v1/migration/status/{migrationId}
     * @param migrationId Unique identifier of the migration
     * @return Response with migration status
     */
    public Response getMigrationStatus(String migrationId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("migrationId", migrationId)
                .when()
                .get(BASE_PATH + "/status/{migrationId}");
    }

    /**
     * Get content map.
     * GET /api/v1/migration/content-map
     * @return Response with content map
     */
    public Response getContentMap() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .get(BASE_PATH + "/content-map");
    }

    /**
     * Get content map by course ID.
     * GET /api/v1/migration/content-map/{courseId}
     * @param courseId Course identifier
     * @return Response with content map for course
     */
    public Response getContentMapByCourse(String courseId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("courseId", courseId)
                .when()
                .get(BASE_PATH + "/content-map/{courseId}");
    }
}
