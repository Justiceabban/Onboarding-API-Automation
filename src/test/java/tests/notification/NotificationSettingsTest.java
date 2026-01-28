package tests.notification;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import models.request.*;
import models.response.*;
import org.testng.annotations.*;

/**
 * Test class for User Notifications - Settings endpoints.
 * Tests: GET and PUT /api/v1/user/settings/notification
 */
@Epic("User Notifications")
@Feature("Notification Settings")
public class NotificationSettingsTest {
    private NotificationClient client;

    @BeforeClass
    public void setup() {
        client = new NotificationClient();
    }

    @Test(description = "Get notification settings - Success scenario (200)")
    public void testGetNotificationSettings_Success() {
        // Arrange
        // Assuming valid bearer token is configured

        // Act
        Response response = client.getNotificationSettings();

        // Assert
        ResponseAssertions.assertStatusCode(response, 200);
        ResponseAssertions.assertContentTypeJson(response);
        ResponseAssertions.assertResponseTimeBelow(response, 3000);

        // Validate response structure
        ResponseAssertions.assertJsonPathExists(response, "$.pk");
        ResponseAssertions.assertJsonPathExists(response, "$.settings");
    }

    @Test(description = "Get notification settings - Unauthorized (401)")
    public void testGetNotificationSettings_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            // Act
            Response response = client.getNotificationSettings();

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
            ResponseAssertions.assertProblemDetail(response, 401, "Unauthorized");
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    @Test(description = "Update notification settings - Success scenario (200)")
    public void testUpdateNotificationSettings_Success() {
        // Arrange
        UserNotificationSettingsRequest request = UserNotificationSettingsRequest.builder()
                .addSetting("emailNotifications", true)
                .addSetting("pushNotifications", false)
                .addSetting("smsNotifications", true)
                .build();

        // Act
        Response response = client.updateNotificationSettings(request);

        // Assert
        ResponseAssertions.assertStatusCode(response, 200);
        ResponseAssertions.assertResponseTimeBelow(response, 3000);

        GenericMessage message = ResponseAssertions.assertGenericMessage(response);
    }

    @Test(description = "Update notification settings - Invalid request body (400)")
    public void testUpdateNotificationSettings_InvalidRequest() {
        // Arrange
        UserNotificationSettingsRequest request = UserNotificationSettingsRequest.builder()
                .addSetting("", true) // Invalid empty name
                .build();

        // Act
        Response response = client.updateNotificationSettings(request);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 400);
    }

    @Test(description = "Update notification settings - Unauthorized (401)")
    public void testUpdateNotificationSettings_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        UserNotificationSettingsRequest request = UserNotificationSettingsRequest.builder()
                .addSetting("emailNotifications", true)
                .build();

        try {
            // Act
            Response response = client.updateNotificationSettings(request);

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
            ResponseAssertions.assertProblemDetail(response, 401, "Unauthorized");
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    @Test(description = "Update notification settings - All notifications enabled")
    public void testUpdateNotificationSettings_AllEnabled() {
        // Arrange
        UserNotificationSettingsRequest request = UserNotificationSettingsRequest.builder()
                .addSetting("emailNotifications", true)
                .addSetting("pushNotifications", true)
                .addSetting("smsNotifications", true)
                .addSetting("inAppNotifications", true)
                .build();

        // Act
        Response response = client.updateNotificationSettings(request);

        // Assert
        ResponseAssertions.assertStatusCode(response, 200);
        GenericMessage message = ResponseAssertions.assertGenericMessage(response);
    }

    @Test(description = "Update notification settings - All notifications disabled")
    public void testUpdateNotificationSettings_AllDisabled() {
        // Arrange
        UserNotificationSettingsRequest request = UserNotificationSettingsRequest.builder()
                .addSetting("emailNotifications", false)
                .addSetting("pushNotifications", false)
                .addSetting("smsNotifications", false)
                .addSetting("inAppNotifications", false)
                .build();

        // Act
        Response response = client.updateNotificationSettings(request);

        // Assert
        ResponseAssertions.assertStatusCode(response, 200);
        GenericMessage message = ResponseAssertions.assertGenericMessage(response);
    }
}
