package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for User Notifications endpoints.
 * Handles notification settings retrieval and updates.
 */
public class NotificationClient {
    private static final String BASE_PATH = "/api/v1/user/settings";

    /**
     * Get notification settings for the authenticated user.
     * GET /api/v1/user/settings/notification
     * @return Response with user notification settings
     */
    public Response getNotificationSettings() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .get(BASE_PATH + "/notification");
    }

    /**
     * Update notification settings for the authenticated user.
     * PUT /api/v1/user/settings/notification
     * @param requestBody UserNotificationSettingsRequest object
     * @return Response with generic message
     */
    public Response updateNotificationSettings(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/notification");
    }
}
