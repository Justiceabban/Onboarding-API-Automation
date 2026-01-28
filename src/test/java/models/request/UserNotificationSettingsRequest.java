package models.request;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

/**
 * Request model for updating user notification settings.
 * PUT /api/v1/user/settings/notification
 */
public class UserNotificationSettingsRequest {
    @JsonProperty("userNotificationSettings")
    private List<UserNotificationSetting> userNotificationSettings;

    // Constructors
    public UserNotificationSettingsRequest() {
        this.userNotificationSettings = new ArrayList<>();
    }

    public UserNotificationSettingsRequest(List<UserNotificationSetting> settings) {
        this.userNotificationSettings = settings;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<UserNotificationSetting> settings = new ArrayList<>();

        public Builder addSetting(String name, boolean state) {
            settings.add(new UserNotificationSetting(name, state));
            return this;
        }

        public Builder settings(List<UserNotificationSetting> settings) {
            this.settings = settings;
            return this;
        }

        public UserNotificationSettingsRequest build() {
            UserNotificationSettingsRequest request = new UserNotificationSettingsRequest();
            request.userNotificationSettings = this.settings;
            return request;
        }
    }

    // Inner class for individual setting
    public static class UserNotificationSetting {
        @JsonProperty("name")
        private String name;

        @JsonProperty("state")
        private boolean state;

        public UserNotificationSetting() {}

        public UserNotificationSetting(String name, boolean state) {
            this.name = name;
            this.state = state;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }
    }

    // Getters and Setters
    public List<UserNotificationSetting> getUserNotificationSettings() {
        return userNotificationSettings;
    }

    public void setUserNotificationSettings(List<UserNotificationSetting> userNotificationSettings) {
        this.userNotificationSettings = userNotificationSettings;
    }
}
