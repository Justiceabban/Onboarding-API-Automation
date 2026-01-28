package models.request;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

/**
 * Request model for reordering stages in a journey.
 * PUT /api/v1/stages/{journeySlug}/reorder
 */
public class StoryReOrderRequest {
    @JsonProperty("storyIds")
    private List<String> storyIds;

    // Constructors
    public StoryReOrderRequest() {
        this.storyIds = new ArrayList<>();
    }

    public StoryReOrderRequest(List<String> storyIds) {
        this.storyIds = storyIds;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> storyIds = new ArrayList<>();

        public Builder storyIds(List<String> storyIds) {
            this.storyIds = storyIds;
            return this;
        }

        public Builder addStoryId(String storyId) {
            this.storyIds.add(storyId);
            return this;
        }

        public StoryReOrderRequest build() {
            StoryReOrderRequest request = new StoryReOrderRequest();
            request.storyIds = this.storyIds;
            return request;
        }
    }

    // Getters and Setters
    public List<String> getStoryIds() {
        return storyIds;
    }

    public void setStoryIds(List<String> storyIds) {
        this.storyIds = storyIds;
    }
}
