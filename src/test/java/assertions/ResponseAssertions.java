package assertions;

import com.jayway.jsonpath.*;
import io.restassured.response.*;
import models.response.*;
import org.slf4j.*;
import org.testng.*;
import utils.*;

import java.util.*;

/**
 * Enterprise-grade assertion utility for Rest Assured API testing.
 * Provides comprehensive validation for status codes, headers, response times,
 * JSON paths, and domain-specific response models.
 */
public class ResponseAssertions {
    private static final Logger logger = LoggerFactory.getLogger(ResponseAssertions.class);

    // ==================== STATUS CODE ASSERTIONS ====================

    /**
     * Assert that the response status code matches the expected value.
     */
    public static void assertStatusCode(Response response, int expectedStatus) {
        int actualStatus = response.getStatusCode();
        logger.info("Asserting status code - Expected: {}, Actual: {}", expectedStatus, actualStatus);

        if (actualStatus != expectedStatus) {
            String errorMsg = String.format(
                    "Status code mismatch!\nExpected: %d\nActual: %d\nResponse Body:\n%s",
                    expectedStatus, actualStatus, response.getBody().asString()
            );
            logger.error(errorMsg);
            Assert.fail(errorMsg);
        }
    }

    /**
     * Assert that the response status code is in the 2xx success range.
     */
    public static void assertSuccessStatusCode(Response response) {
        int actualStatus = response.getStatusCode();
        logger.info("Asserting success status code (2xx) - Actual: {}", actualStatus);
        Assert.assertTrue(actualStatus >= 200 && actualStatus < 300,
                "Expected success status code (2xx), but got: " + actualStatus);
    }

    /**
     * Assert that the response status code is one of the expected values.
     */
    public static void assertStatusCodeIn(Response response, int... expectedStatuses) {
        int actualStatus = response.getStatusCode();
        logger.info("Asserting status code is one of: {} - Actual: {}", expectedStatuses, actualStatus);

        for (int expected : expectedStatuses) {
            if (actualStatus == expected) {
                return;
            }
        }

        String errorMsg = String.format(
                "Status code not in expected set!\nExpected one of: %s\nActual: %d\nResponse Body:\n%s",
                java.util.Arrays.toString(expectedStatuses), actualStatus, response.getBody().asString()
        );
        logger.error(errorMsg);
        Assert.fail(errorMsg);
    }

    // ==================== HEADER ASSERTIONS ====================

    /**
     * Assert that a specific header exists in the response.
     */
    public static void assertHeaderExists(Response response, String headerName) {
        logger.info("Asserting header exists: {}", headerName);
        Assert.assertNotNull(response.getHeader(headerName),
                "Expected header '" + headerName + "' not found in response");
    }

    /**
     * Assert that a specific header has the expected value.
     */
    public static void assertHeaderEquals(Response response, String headerName, String expectedValue) {
        logger.info("Asserting header {} equals {}", headerName, expectedValue);
        String actualValue = response.getHeader(headerName);
        Assert.assertEquals(actualValue, expectedValue,
                String.format("Header '%s' value mismatch. Expected: %s, Actual: %s",
                        headerName, expectedValue, actualValue));
    }

    /**
     * Assert that the response Content-Type is JSON.
     */
    public static void assertContentTypeJson(Response response) {
        logger.info("Asserting Content-Type is JSON");
        String contentType = response.getContentType();
        Assert.assertTrue(contentType != null && contentType.contains("application/json"),
                "Expected Content-Type to contain 'application/json', but got: " + contentType);
    }

    // ==================== RESPONSE TIME ASSERTIONS ====================

    /**
     * Assert that the response time is below the specified threshold in milliseconds.
     */
    public static void assertResponseTimeBelow(Response response, long thresholdMs) {
        long actualTime = response.getTime();
        logger.info("Asserting response time below {}ms - Actual: {}ms", thresholdMs, actualTime);
        Assert.assertTrue(actualTime < thresholdMs,
                String.format("Response time exceeded threshold. Expected: <%d ms, Actual: %d ms",
                        thresholdMs, actualTime));
    }

    // ==================== JSON PATH ASSERTIONS ====================

    /**
     * Assert that a JSON path exists in the response.
     */
    public static void assertJsonPathExists(Response response, String jsonPath) {
        logger.info("Asserting JSON path exists: {}", jsonPath);
        try {
            Object value = JsonPath.read(response.getBody().asString(), jsonPath);
            Assert.assertNotNull(value, "JSON path '" + jsonPath + "' returned null");
        } catch (Exception e) {
            Assert.fail("JSON path '" + jsonPath + "' not found in response: " + e.getMessage());
        }
    }

    /**
     * Assert that a JSON path value equals the expected value.
     */
    public static void assertJsonPathEquals(Response response, String jsonPath, Object expectedValue) {
        logger.info("Asserting JSON path {} equals {}", jsonPath, expectedValue);
        try {
            Object actualValue = JsonPath.read(response.getBody().asString(), jsonPath);
            Assert.assertEquals(actualValue, expectedValue,
                    String.format("JSON path '%s' value mismatch. Expected: %s, Actual: %s",
                            jsonPath, expectedValue, actualValue));
        } catch (Exception e) {
            Assert.fail("Failed to extract JSON path '" + jsonPath + "': " + e.getMessage());
        }
    }

    /**
     * Extract a value from JSON path.
     */
    public static <T> T extractJsonPath(Response response, String jsonPath) {
        logger.info("Extracting JSON path: {}", jsonPath);
        try {
            return JsonPath.read(response.getBody().asString(), jsonPath);
        } catch (Exception e) {
            throw new AssertionError("Failed to extract JSON path '" + jsonPath + "': " + e.getMessage(), e);
        }
    }

    // ==================== COLLECTION ASSERTIONS ====================

    /**
     * Assert that a JSON array at the given path has the expected size.
     */
    public static void assertArraySize(Response response, String jsonPath, int expectedSize) {
        logger.info("Asserting array at {} has size {}", jsonPath, expectedSize);
        try {
            List<?> array = JsonPath.read(response.getBody().asString(), jsonPath);
            Assert.assertEquals(array.size(), expectedSize,
                    String.format("Array size mismatch at '%s'. Expected: %d, Actual: %d",
                            jsonPath, expectedSize, array.size()));
        } catch (Exception e) {
            Assert.fail("Failed to extract array from JSON path '" + jsonPath + "': " + e.getMessage());
        }
    }

    /**
     * Assert that a JSON array is not empty.
     */
    public static void assertArrayNotEmpty(Response response, String jsonPath) {
        logger.info("Asserting array at {} is not empty", jsonPath);
        try {
            List<?> array = JsonPath.read(response.getBody().asString(), jsonPath);
            Assert.assertFalse(array.isEmpty(), "Array at '" + jsonPath + "' should not be empty");
        } catch (Exception e) {
            Assert.fail("Failed to extract array from JSON path '" + jsonPath + "': " + e.getMessage());
        }
    }

    // ==================== MODEL-SPECIFIC ASSERTIONS ====================

    /**
     * Assert and validate ProblemDetail response structure.
     */
    public static ProblemDetail assertProblemDetail(Response response) {
        logger.info("Asserting ProblemDetail response structure");
        assertContentTypeJson(response);

        try {
            ProblemDetail problemDetail = JsonUtils.fromJson(response.getBody().asString(), ProblemDetail.class);
            Assert.assertNotNull(problemDetail.getType(), "ProblemDetail 'type' field is null");
            Assert.assertNotNull(problemDetail.getTitle(), "ProblemDetail 'title' field is null");
            Assert.assertTrue(problemDetail.getStatus() > 0, "ProblemDetail 'status' is invalid");

            logger.info("ProblemDetail validated - Type: {}, Title: {}, Status: {}",
                    problemDetail.getType(), problemDetail.getTitle(), problemDetail.getStatus());

            return problemDetail;
        } catch (Exception e) {
            logger.error("Failed to parse ProblemDetail response: {}", e.getMessage());
            Assert.fail("Response is not a valid ProblemDetail: " + e.getMessage());
            return null;
        }
    }

    /**
     * Assert ProblemDetail with specific expected values.
     */
    public static void assertProblemDetail(Response response, int expectedStatus, String expectedTitle) {
        ProblemDetail problemDetail = assertProblemDetail(response);
        Assert.assertEquals(problemDetail.getStatus(), expectedStatus,
                "ProblemDetail status mismatch");
        Assert.assertEquals(problemDetail.getTitle(), expectedTitle,
                "ProblemDetail title mismatch");
    }

    /**
     * Assert and validate GenericMessage response structure.
     */
    public static GenericMessage assertGenericMessage(Response response) {
        logger.info("Asserting GenericMessage response structure");
        assertContentTypeJson(response);

        try {
            GenericMessage genericMessage = JsonUtils.fromJson(response.getBody().asString(), GenericMessage.class);
            Assert.assertNotNull(genericMessage.getMessage(), "GenericMessage 'message' field is null");

            logger.info("GenericMessage validated - Message: {}", genericMessage.getMessage());

            return genericMessage;
        } catch (Exception e) {
            logger.error("Failed to parse GenericMessage response: {}", e.getMessage());
            Assert.fail("Response is not a valid GenericMessage: " + e.getMessage());
            return null;
        }
    }

    /**
     * Assert GenericMessage with expected message content.
     */
    public static void assertGenericMessage(Response response, String expectedMessage) {
        GenericMessage genericMessage = assertGenericMessage(response);
        Assert.assertEquals(genericMessage.getMessage(), expectedMessage,
                "GenericMessage content mismatch");
    }

    /**
     * Assert GenericMessage contains expected substring.
     */
    public static void assertGenericMessageContains(Response response, String expectedSubstring) {
        GenericMessage genericMessage = assertGenericMessage(response);
        Assert.assertTrue(genericMessage.getMessage().contains(expectedSubstring),
                String.format("GenericMessage does not contain '%s'. Actual: %s",
                        expectedSubstring, genericMessage.getMessage()));
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Log full response details (for debugging).
     */
    public static void logResponse(Response response) {
        logger.info("Response Status: {}", response.getStatusCode());
        logger.info("Response Time: {}ms", response.getTime());
        logger.info("Response Headers: {}", response.getHeaders());
        logger.info("Response Body:\n{}", response.getBody().asString());
    }

    /**
     * Assert that response body is not empty.
     */
    public static void assertBodyNotEmpty(Response response) {
        logger.info("Asserting response body is not empty");
        String body = response.getBody().asString();
        Assert.assertNotNull(body, "Response body is null");
        Assert.assertFalse(body.trim().isEmpty(), "Response body is empty");
    }

    /**
     * Assert that response body contains expected substring.
     */
    public static void assertBodyContains(Response response, String expectedSubstring) {
        logger.info("Asserting response body contains: {}", expectedSubstring);
        String body = response.getBody().asString();
        Assert.assertTrue(body.contains(expectedSubstring),
                String.format("Response body does not contain '%s'", expectedSubstring));
    }
}
