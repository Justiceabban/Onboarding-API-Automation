package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Favourites endpoints.
 * Handles user's favourite pages management.
 */
public class FavouriteClient {
    private static final String BASE_PATH = "/api/v1/favourites";

    /**
     * Get user's favourite pages.
     * GET /api/v1/favourites
     * @return Response with favourite pages list
     */
    public Response getFavourites() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .get(BASE_PATH);
    }

    /**
     * Add page to favourites.
     * POST /api/v1/favourites/page/{pageId}
     * @param pageId Unique identifier of the page
     * @return Response with generic message
     */
    public Response addToFavourites(String pageId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("pageId", pageId)
                .when()
                .post(BASE_PATH + "/page/{pageId}");
    }

    /**
     * Remove page from favourites.
     * DELETE /api/v1/favourites/page/{pageId}
     * @param pageId Unique identifier of the page
     * @return Response
     */
    public Response removeFromFavourites(String pageId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("pageId", pageId)
                .when()
                .delete(BASE_PATH + "/page/{pageId}");
    }

    /**
     * Check if page is in favourites.
     * GET /api/v1/favourites/page/{pageId}/status
     * @param pageId Unique identifier of the page
     * @return Response with favourite status
     */
    public Response isFavourite(String pageId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("pageId", pageId)
                .when()
                .get(BASE_PATH + "/page/{pageId}/status");
    }

    /**
     * Clear all favourites.
     * DELETE /api/v1/favourites
     * @return Response
     */
    public Response clearAllFavourites() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .delete(BASE_PATH);
    }
}
