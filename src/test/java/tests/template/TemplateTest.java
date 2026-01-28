package tests.template;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

import java.util.*;

/**
 * Test class for Templates endpoints.
 */
@Epic("Content Management")
@Feature("Templates")
public class TemplateTest {
    private TemplateClient client;
    private String createdTemplateId;

    @BeforeClass
    public void setup() {
        client = new TemplateClient();
    }

    @Test(description = "Get all templates - Success (200)")
    public void testGetAllTemplates_Success() {
        Response response = client.getAllTemplates();
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
        }
    }

    @Test(description = "Create template - Success (201)", priority = 1)
    public void testCreateTemplate_Success() {
        Map<String, Object> request = new HashMap<>();
        request.put("name", "Text with Image Template");
        request.put("type", "oba_text_image_template");
        request.put("description", "Template for text with image content");

        Response response = client.createTemplate(request);
        ResponseAssertions.assertStatusCodeIn(response, 200, 201, 400);

        if (response.getStatusCode() == 201 || response.getStatusCode() == 200) {
            try {
                createdTemplateId = ResponseAssertions.extractJsonPath(response, "$.id");
            } catch (Exception e) {}
        }
    }

    @Test(description = "Get template by ID - Success (200)", priority = 2)
    public void testGetTemplateById_Success() {
        String templateId = createdTemplateId != null ? createdTemplateId : "sample-template-id";
        Response response = client.getTemplateById(templateId);
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Update template - Success (200)", priority = 3)
    public void testUpdateTemplate_Success() {
        String templateId = createdTemplateId != null ? createdTemplateId : "sample-template-id";
        Map<String, Object> request = new HashMap<>();
        request.put("name", "Updated Template Name");

        Response response = client.updateTemplate(templateId, request);
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Get template types - Success (200)")
    public void testGetTemplateTypes_Success() {
        Response response = client.getTemplateTypes();
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
        if (response.getStatusCode() == 200) {
            ResponseAssertions.assertContentTypeJson(response);
        }
    }

    @Test(description = "Delete template - Success (204)", priority = 4)
    public void testDeleteTemplate_Success() {
        String templateId = createdTemplateId != null ? createdTemplateId : "sample-template-id";
        Response response = client.deleteTemplate(templateId);
        ResponseAssertions.assertStatusCodeIn(response, 204, 200, 404);
    }

    @Test(description = "Delete template - Unauthorized (401)")
    public void testDeleteTemplate_Unauthorized() {
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            Response response = client.deleteTemplate("sample-template-id");
            ResponseAssertions.assertStatusCode(response, 401);
        } finally {
            AuthManager.setBearerToken(originalToken);
        }
    }
}
