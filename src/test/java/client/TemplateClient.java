package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Templates endpoints.
 * Handles content template operations.
 */
public class TemplateClient {
    private static final String BASE_PATH = "/api/v1/templates";

    /**
     * Get all templates.
     * GET /api/v1/templates
     * @return Response with templates list
     */
    public Response getAllTemplates() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .get(BASE_PATH);
    }

    /**
     * Get template by ID.
     * GET /api/v1/templates/{templateId}
     * @param templateId Unique identifier of the template
     * @return Response with template details
     */
    public Response getTemplateById(String templateId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("templateId", templateId)
                .when()
                .get(BASE_PATH + "/{templateId}");
    }

    /**
     * Create a new template.
     * POST /api/v1/templates
     * @param requestBody Template creation request
     * @return Response with created template
     */
    public Response createTemplate(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH);
    }

    /**
     * Update template.
     * PUT /api/v1/templates/{templateId}
     * @param templateId Unique identifier of the template
     * @param requestBody Template update request
     * @return Response with generic message
     */
    public Response updateTemplate(String templateId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("templateId", templateId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{templateId}");
    }

    /**
     * Delete template.
     * DELETE /api/v1/templates/{templateId}
     * @param templateId Unique identifier of the template
     * @return Response
     */
    public Response deleteTemplate(String templateId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("templateId", templateId)
                .when()
                .delete(BASE_PATH + "/{templateId}");
    }

    /**
     * Get template types.
     * GET /api/v1/templates/types
     * @return Response with template types
     */
    public Response getTemplateTypes() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .get(BASE_PATH + "/types");
    }
}
