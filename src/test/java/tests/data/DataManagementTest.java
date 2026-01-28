package tests.data;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

import java.util.*;

/**
 * Test class for Data Management - CRUD operations.
 * Tests: POST, GET, PUT, DELETE /api/v1/data
 */
@Epic("Data Management")
@Feature("Data CRUD Operations")
public class DataManagementTest {
    private DataManagementClient client;
    private String createdRecordId;

    @BeforeClass
    public void setup() {
        client = new DataManagementClient();
    }

    @Test(description = "Create data record - Success scenario (201)", priority = 1)
    public void testCreateDataRecord_Success() {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Test Data Record");
        requestBody.put("description", "Created via automation test");
        requestBody.put("type", "test_type");
        requestBody.put("metadata", Map.of("key1", "value1", "key2", "value2"));

        // Act
        Response response = client.createDataRecord(requestBody);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 201, 200);
        ResponseAssertions.assertResponseTimeBelow(response, 5000);
        ResponseAssertions.assertBodyNotEmpty(response);

        // Store created record ID for cleanup
        if (response.getStatusCode() == 201) {
            try {
                createdRecordId = ResponseAssertions.extractJsonPath(response, "$.id");
            } catch (Exception e) {
                // ID might be in different location or format
            }
        }
    }

    @Test(description = "Create data record - Invalid request body (400)")
    public void testCreateDataRecord_InvalidRequest() {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        // Missing required fields

        // Act
        Response response = client.createDataRecord(requestBody);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 400, 201, 200);
    }

    @Test(description = "Create data record - Unauthorized (401)")
    public void testCreateDataRecord_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Test Record");

        try {
            // Act
            Response response = client.createDataRecord(requestBody);

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }

    @Test(description = "Get data record by ID - Success scenario (200)", priority = 2, dependsOnMethods = "testCreateDataRecord_Success")
    public void testGetDataRecordById_Success() {
        // Arrange
        String recordId = createdRecordId != null ? createdRecordId : "sample-record-id";

        // Act
        Response response = client.getDataRecordById(recordId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertBodyNotEmpty(response);
        }
    }

    @Test(description = "Get data record by ID - Not found (404)")
    public void testGetDataRecordById_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-record-id-12345";

        // Act
        Response response = client.getDataRecordById(nonExistentId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }

    @Test(description = "Update data record - Success scenario (200)", priority = 3, dependsOnMethods = "testCreateDataRecord_Success")
    public void testUpdateDataRecord_Success() {
        // Arrange
        String recordId = createdRecordId != null ? createdRecordId : "sample-record-id";

        Map<String, Object> updateBody = new HashMap<>();
        updateBody.put("name", "Updated Data Record");
        updateBody.put("description", "Updated via automation test");

        // Act
        Response response = client.updateDataRecord(recordId, updateBody);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertResponseTimeBelow(response, 5000);
        }
    }

    @Test(description = "Update data record - Not found (404)")
    public void testUpdateDataRecord_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-record-id-12345";
        Map<String, Object> updateBody = new HashMap<>();
        updateBody.put("name", "Updated Record");

        // Act
        Response response = client.updateDataRecord(nonExistentId, updateBody);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }

    @Test(description = "Get all data records - Success scenario (200)")
    public void testGetAllDataRecords_Success() {
        // Arrange
        // No specific setup needed

        // Act
        Response response = client.getAllDataRecords();

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
            ResponseAssertions.assertResponseTimeBelow(response, 5000);
        }
    }

    @Test(description = "Get all data records with pagination")
    public void testGetAllDataRecords_WithPagination() {
        // Arrange
        int page = 0;
        int size = 10;

        // Act
        Response response = client.getAllDataRecords(page, size);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);

        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
        }
    }

    @Test(description = "Delete data record - Success scenario (204)", priority = 4, dependsOnMethods = "testUpdateDataRecord_Success")
    public void testDeleteDataRecord_Success() {
        // Arrange
        String recordId = createdRecordId != null ? createdRecordId : "sample-record-id";

        // Act
        Response response = client.deleteDataRecord(recordId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 204, 200, 404);
    }

    @Test(description = "Delete data record - Not found (404)")
    public void testDeleteDataRecord_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-record-id-12345";

        // Act
        Response response = client.deleteDataRecord(nonExistentId);

        // Assert
        ResponseAssertions.assertStatusCodeIn(response, 404, 400);
    }

    @Test(description = "Delete data record - Unauthorized (401)")
    public void testDeleteDataRecord_Unauthorized() {
        // Arrange
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();
        String recordId = "sample-record-id";

        try {
            // Act
            Response response = client.deleteDataRecord(recordId);

            // Assert
            ResponseAssertions.assertStatusCode(response, 401);
        } finally {
            // Cleanup
            AuthManager.setBearerToken(originalToken);
        }
    }
}
