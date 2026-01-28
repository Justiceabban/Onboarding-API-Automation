package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Pages endpoints.
 * Handles page retrieval and management operations.
 */
public class PageClient {
    private static final String BASE_PATH = "/api/v1/pages";

    /**
     * Get a page by slug.
     * GET /api/v1/pages/{pageSlug}/view
     * @param pageSlug Page slug identifier
     * @param language Language code (optional, defaults to en-gb)
     * @return Response with page details
     */
    public Response getPageBySlug(String pageSlug, String language) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("pageSlug", pageSlug)
                .queryParam("lang", language)
                .when()
                .get(BASE_PATH + "/{pageSlug}/view");
    }

    /**
     * Get a page by slug with default language.
     * @param pageSlug Page slug identifier
     * @return Response with page details
     */
    public Response getPageBySlug(String pageSlug) {
        return getPageBySlug(pageSlug, "en-gb");
    }

    /**
     * Get all pages in a chapter.
     * GET /api/v1/pages/{chapter_slug}
     * @param chapterSlug Chapter slug identifier
     * @param language Language code (optional, defaults to en-gb)
     * @return Response with list of pages
     */
    public Response getAllPagesInChapter(String chapterSlug, String language) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("chapter_slug", chapterSlug)
                .queryParam("lang", language)
                .when()
                .get(BASE_PATH + "/{chapter_slug}");
    }

    /**
     * Get all pages in a chapter with default language.
     * @param chapterSlug Chapter slug identifier
     * @return Response with list of pages
     */
    public Response getAllPagesInChapter(String chapterSlug) {
        return getAllPagesInChapter(chapterSlug, "en-gb");
    }

    /**
     * Update a page.
     * PUT /api/v1/pages/{pageId}
     * @param pageId Unique identifier of the page
     * @param requestBody Page update request object
     * @return Response with generic message
     */
    public Response updatePage(String pageId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("pageId", pageId)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{pageId}");
    }
}
