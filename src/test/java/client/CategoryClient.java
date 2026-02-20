package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Categories endpoints.
 * Handles category CRUD operations for journey groupings.
 */
public class CategoryClient {
    private static final String BASE_PATH = "/api/v1/categories";

    /**
     * Create a new category.
     * POST /api/v1/categories
     * @param requestBody CategoryRequest object
     * @return Response with created category
     */
    public Response createCategory(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH);
    }

    /**
     * Create a new category within a journey.
     * POST /api/v1/categories/{journeySlug}
     * @param journeySlug Slug identifier of the journey
     * @param requestBody CategoryRequest object
     * @return Response with created category
     */
    public Response createCategory(String journeySlug, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("journeySlug", journeySlug)
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/{journeySlug}");
    }

    /**
     * Get category by ID.
     * GET /api/v1/categories/{categoryId}
     * @param categoryId Unique identifier of the category
     * @return Response with category details
     */
    public Response getCategoryById(String categoryId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("categoryId", categoryId)
                .when()
                .get(BASE_PATH + "/{categoryId}");
    }

    /**
     * Update category.
     * PUT /api/v1/categories/{categoryId}
     * @param categoryId Unique identifier of the category
     * @param requestBody CategoryRequest object
     * @return Response with generic message
     */
    public Response updateCategory(String categoryId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("categoryId", categoryId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{categoryId}");
    }

    /**
     * Delete category.
     * DELETE /api/v1/categories/{categoryId}
     * @param categoryId Unique identifier of the category
     * @return Response
     */
    public Response deleteCategory(String categoryId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("categoryId", categoryId)
                .when()
                .delete(BASE_PATH + "/{categoryId}");
    }

    /**
     * Get all categories for a journey.
     * GET /api/v1/categories/journey/{journeyId}
     * @param journeyId Unique identifier of the journey
     * @return Response with list of categories
     */
    public Response getCategoriesByJourney(String journeyId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("journeyId", journeyId)
                .when()
                .get(BASE_PATH + "/journey/{journeyId}");
    }
}
