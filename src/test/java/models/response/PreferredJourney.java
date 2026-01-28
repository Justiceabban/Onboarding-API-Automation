package models.response;

import com.fasterxml.jackson.annotation.*;

/**
 * Preferred Journey response model.
 * GET /api/v1/users/preferred-journey
 */
public class PreferredJourney {
    @JsonProperty("storyblokId")
    private String storyblokId;

    @JsonProperty("defaultLanguage")
    private String defaultLanguage;

    // Constructors
    public PreferredJourney() {}

    public PreferredJourney(String storyblokId, String defaultLanguage) {
        this.storyblokId = storyblokId;
        this.defaultLanguage = defaultLanguage;
    }

    // Getters and Setters
    public String getStoryblokId() {
        return storyblokId;
    }

    public void setStoryblokId(String storyblokId) {
        this.storyblokId = storyblokId;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    @Override
    public String toString() {
        return "PreferredJourney{" +
                "storyblokId='" + storyblokId + '\'' +
                ", defaultLanguage='" + defaultLanguage + '\'' +
                '}';
    }
}
