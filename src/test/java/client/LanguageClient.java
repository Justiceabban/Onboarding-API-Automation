package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Language endpoints.
 * Handles supported language operations.
 */
public class LanguageClient {
    private static final String BASE_PATH = "/api/v1/languages";

    /**
     * Get all supported languages.
     * GET /api/v1/languages
     * @return Response with list of languages
     */
    public Response getAllLanguages() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .get(BASE_PATH);
    }

    /**
     * Get language by code.
     * GET /api/v1/languages/{languageCode}
     * @param languageCode Language code (e.g., en, de, fr)
     * @return Response with language details
     */
    public Response getLanguageByCode(String languageCode) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("languageCode", languageCode)
                .when()
                .get(BASE_PATH + "/{languageCode}");
    }

    /**
     * Add a new language.
     * POST /api/v1/languages
     * @param requestBody Language request object
     * @return Response with created language
     */
    public Response addLanguage(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH);
    }

    /**
     * Update language.
     * PUT /api/v1/languages/{languageCode}
     * @param languageCode Language code
     * @param requestBody Language update request
     * @return Response with generic message
     */
    public Response updateLanguage(String languageCode, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("languageCode", languageCode)
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/{languageCode}");
    }
}
