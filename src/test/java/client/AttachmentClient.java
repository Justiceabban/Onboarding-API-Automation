package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for Attachments endpoints.
 * Handles file attachment management.
 */
public class AttachmentClient {
    private static final String BASE_PATH = "/api/v1/attachments";

    /**
     * Upload an attachment.
     * POST /api/v1/attachments
     * @param file File to upload
     * @param contentType Content type of the file
     * @return Response with attachment details
     */
    public Response uploadAttachment(byte[] file, String contentType) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .contentType(contentType)
                .body(file)
                .when()
                .post(BASE_PATH);
    }

    /**
     * Get attachment by ID.
     * GET /api/v1/attachments/{attachmentId}
     * @param attachmentId Unique identifier of the attachment
     * @return Response with attachment content
     */
    public Response getAttachment(String attachmentId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("attachmentId", attachmentId)
                .when()
                .get(BASE_PATH + "/{attachmentId}");
    }

    /**
     * Delete attachment.
     * DELETE /api/v1/attachments/{attachmentId}
     * @param attachmentId Unique identifier of the attachment
     * @return Response
     */
    public Response deleteAttachment(String attachmentId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("attachmentId", attachmentId)
                .when()
                .delete(BASE_PATH + "/{attachmentId}");
    }

    /**
     * Get attachments for a resource.
     * GET /api/v1/attachments/resource/{resourceId}
     * @param resourceId Unique identifier of the resource
     * @return Response with list of attachments
     */
    public Response getAttachmentsForResource(String resourceId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("resourceId", resourceId)
                .when()
                .get(BASE_PATH + "/resource/{resourceId}");
    }
}
