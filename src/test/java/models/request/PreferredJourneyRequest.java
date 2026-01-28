package models.request;

import com.fasterxml.jackson.annotation.*;

/**
 * Request model for updating preferred journey.
 * POST/PUT /api/v1/users/preferred-journey
 */
public class PreferredJourneyRequest {
    @JsonProperty("journeyId")
    private String journeyId;

    // Constructors
    public PreferredJourneyRequest() {}

    public PreferredJourneyRequest(String journeyId) {
        this.journeyId = journeyId;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String journeyId;

        public Builder journeyId(String journeyId) {
            this.journeyId = journeyId;
            return this;
        }

        public PreferredJourneyRequest build() {
            PreferredJourneyRequest request = new PreferredJourneyRequest();
            request.journeyId = this.journeyId;
            return request;
        }
    }

    // Getters and Setters
    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }
}
