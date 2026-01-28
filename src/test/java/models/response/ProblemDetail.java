package models.response;

import com.fasterxml.jackson.annotation.*;

/**
 * Problem Detail response model (RFC 7807).
 * Used for error responses across the API.
 */
public class ProblemDetail {
    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("status")
    private int status;

    @JsonProperty("detail")
    private String detail;

    @JsonProperty("instance")
    private String instance;

    @JsonProperty("properties")
    private Object properties;

    // Constructors
    public ProblemDetail() {}

    public ProblemDetail(String type, String title, int status, String detail) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "ProblemDetail{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", detail='" + detail + '\'' +
                ", instance='" + instance + '\'' +
                ", properties=" + properties +
                '}';
    }
}
