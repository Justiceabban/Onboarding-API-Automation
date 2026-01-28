package client;

import config.*;
import io.restassured.response.*;

import java.io.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Asset Management endpoints.
 * Handles asset upload, retrieval, and management.
 */
public class AssetManagementClient {
    private static final String BASE_PATH = "/api/v1/upload";

    /**
     * Upload an asset.
     * POST /api/v1/upload
     * @param file File to upload
     * @return Response with asset details
     */
    public Response uploadAsset(File file) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .contentType("multipart/form-data")
                .multiPart("file", file)
                .when()
                .post(BASE_PATH);
    }

    /**
     * Upload asset with additional metadata.
     * POST /api/v1/upload
     * @param file File to upload
     * @param description Asset description
     * @return Response with asset details
     */
    public Response uploadAssetWithMetadata(File file, String description) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .contentType("multipart/form-data")
                .multiPart("file", file)
                .multiPart("description", description)
                .when()
                .post(BASE_PATH);
    }

    /**
     * Get asset by ID.
     * GET /api/v1/upload/{assetId}
     * @param assetId Unique identifier of the asset
     * @return Response with asset content
     */
    public Response getAsset(String assetId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("assetId", assetId)
                .when()
                .get(BASE_PATH + "/{assetId}");
    }

    /**
     * Delete asset.
     * DELETE /api/v1/upload/{assetId}
     * @param assetId Unique identifier of the asset
     * @return Response
     */
    public Response deleteAsset(String assetId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("assetId", assetId)
                .when()
                .delete(BASE_PATH + "/{assetId}");
    }

    /**
     * Get all assets.
     * GET /api/v1/upload
     * @param page Page number
     * @param size Page size
     * @return Response with assets list
     */
    public Response getAllAssets(int page, int size) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(BASE_PATH);
    }

    /**
     * Get all assets with default pagination.
     * @return Response with assets list
     */
    public Response getAllAssets() {
        return getAllAssets(0, 20);
    }
}
