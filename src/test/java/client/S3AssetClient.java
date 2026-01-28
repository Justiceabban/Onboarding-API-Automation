package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for S3 Assets endpoints.
 * Handles S3 asset access and presigned URL generation.
 */
public class S3AssetClient {
    private static final String BASE_PATH = "/api/v1/s3";

    /**
     * Get presigned URL for S3 asset.
     * GET /api/v1/s3/presigned-url
     * @param objectKey S3 object key
     * @return Response with presigned URL
     */
    public Response getPresignedUrl(String objectKey) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("objectKey", objectKey)
                .when()
                .get(BASE_PATH + "/presigned-url");
    }

    /**
     * Get presigned upload URL.
     * POST /api/v1/s3/presigned-upload
     * @param requestBody Upload request with object key and metadata
     * @return Response with presigned upload URL
     */
    public Response getPresignedUploadUrl(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/presigned-upload");
    }

    /**
     * Get asset metadata.
     * GET /api/v1/s3/metadata
     * @param objectKey S3 object key
     * @return Response with asset metadata
     */
    public Response getAssetMetadata(String objectKey) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("objectKey", objectKey)
                .when()
                .get(BASE_PATH + "/metadata");
    }

    /**
     * Delete S3 asset.
     * DELETE /api/v1/s3/asset
     * @param objectKey S3 object key
     * @return Response
     */
    public Response deleteS3Asset(String objectKey) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("objectKey", objectKey)
                .when()
                .delete(BASE_PATH + "/asset");
    }

    /**
     * List S3 assets.
     * GET /api/v1/s3/assets
     * @param prefix Object key prefix for filtering
     * @return Response with list of assets
     */
    public Response listAssets(String prefix) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("prefix", prefix)
                .when()
                .get(BASE_PATH + "/assets");
    }

    /**
     * List all S3 assets.
     * GET /api/v1/s3/assets
     * @return Response with list of assets
     */
    public Response listAllAssets() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .get(BASE_PATH + "/assets");
    }
}
