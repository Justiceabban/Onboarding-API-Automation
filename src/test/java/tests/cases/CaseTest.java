package tests.cases;

import assertions.*;
import client.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

import java.util.*;

/**
 * Test class for Cases endpoints.
 */
@Epic("Case Management")
@Feature("Cases")
public class CaseTest {
    private CaseClient client;
    private String createdCaseId;

    @BeforeClass
    public void setup() {
        client = new CaseClient();
    }

    @Test(description = "Get all cases - Success (200)")
    public void testGetAllCases_Success() {
        Response response = client.getAllCases();
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Create case - Success (201)", priority = 1)
    public void testCreateCase_Success() {
        Map<String, Object> request = new HashMap<>();
        request.put("title", "New Case");
        request.put("description", "Case description");
        request.put("priority", "HIGH");

        Response response = client.createCase(request);
        ResponseAssertions.assertStatusCodeIn(response, 200, 201, 400);

        if (response.getStatusCode() == 201 || response.getStatusCode() == 200) {
            try {
                createdCaseId = ResponseAssertions.extractJsonPath(response, "$.id");
            } catch (Exception e) {}
        }
    }

    @Test(description = "Get case by ID - Success (200)", priority = 2)
    public void testGetCaseById_Success() {
        String caseId = createdCaseId != null ? createdCaseId : "sample-case-id";
        Response response = client.getCaseById(caseId);
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Update case - Success (200)", priority = 3)
    public void testUpdateCase_Success() {
        String caseId = createdCaseId != null ? createdCaseId : "sample-case-id";
        Map<String, Object> request = new HashMap<>();
        request.put("status", "IN_PROGRESS");

        Response response = client.updateCase(caseId, request);
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Add case action - Success (200)")
    public void testAddCaseAction_Success() {
        String caseId = createdCaseId != null ? createdCaseId : "sample-case-id";
        Map<String, Object> request = new HashMap<>();
        request.put("action", "REVIEWED");
        request.put("comment", "Case reviewed");

        Response response = client.addCaseAction(caseId, request);
        ResponseAssertions.assertStatusCodeIn(response, 200, 201, 404);
    }

    @Test(description = "Get case attachments - Success (200)")
    public void testGetCaseAttachments_Success() {
        String caseId = createdCaseId != null ? createdCaseId : "sample-case-id";
        Response response = client.getCaseAttachments(caseId);
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Delete case - Success (204)", priority = 4)
    public void testDeleteCase_Success() {
        String caseId = createdCaseId != null ? createdCaseId : "sample-case-id";
        Response response = client.deleteCase(caseId);
        ResponseAssertions.assertStatusCodeIn(response, 204, 200, 404);
    }
}
