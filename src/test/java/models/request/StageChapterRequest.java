package models.request;

import com.fasterxml.jackson.annotation.*;

/**
 * Request model for creating/updating stage chapters.
 * POST/PUT /api/v1/stages
 */
public class StageChapterRequest {
    @JsonProperty("title")
    private String title;

    @JsonProperty("assetId")
    private String assetId;

    @JsonProperty("assetDescription")
    private String assetDescription;

    @JsonProperty("status")
    private String status; // LIVE, DRAFT, ARCHIVED

    @JsonProperty("language")
    private String language;

    @JsonProperty("isInternal")
    private Boolean isInternal;

    // Constructors
    public StageChapterRequest() {}

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String assetId;
        private String assetDescription;
        private String status;
        private String language;
        private Boolean isInternal;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder assetId(String assetId) {
            this.assetId = assetId;
            return this;
        }

        public Builder assetDescription(String assetDescription) {
            this.assetDescription = assetDescription;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder isInternal(Boolean isInternal) {
            this.isInternal = isInternal;
            return this;
        }

        public StageChapterRequest build() {
            StageChapterRequest request = new StageChapterRequest();
            request.title = this.title;
            request.assetId = this.assetId;
            request.assetDescription = this.assetDescription;
            request.status = this.status;
            request.language = this.language;
            request.isInternal = this.isInternal;
            return request;
        }
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getIsInternal() {
        return isInternal;
    }

    public void setIsInternal(Boolean isInternal) {
        this.isInternal = isInternal;
    }
}
