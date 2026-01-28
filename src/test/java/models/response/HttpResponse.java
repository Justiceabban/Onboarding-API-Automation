package models.response;

import com.fasterxml.jackson.annotation.*;

/**
 * HTTP Response model for error scenarios.
 */
public class HttpResponse {
    @JsonProperty("httpStatusCode")
    private int httpStatusCode;

    @JsonProperty("httpStatus")
    private String httpStatus;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("message")
    private String message;

    // Constructors
    public HttpResponse() {}

    // Getters and Setters
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "httpStatusCode=" + httpStatusCode +
                ", httpStatus='" + httpStatus + '\'' +
                ", reason='" + reason + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
