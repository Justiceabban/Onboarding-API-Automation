package models.response;

import com.fasterxml.jackson.annotation.*;

/**
 * Generic message response model.
 * Used across multiple endpoints for success messages.
 */
public class GenericMessage {
    @JsonProperty("message")
    private String message;

    // Constructors
    public GenericMessage() {}

    public GenericMessage(String message) {
        this.message = message;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GenericMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
