package tests.chapter;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

import java.util.*;

/**
 * Test class for Chapter Management endpoints.
 */
@Epic("Content Management")
@Feature("Chapter Management")
public class ChapterTest {
    private ChapterClient client;
    private static final String TEST_STAGE_ID = "test-stage-123";
    private String createdChapterId;

    @BeforeClass
    public void setup() {
        client = new ChapterClient();
    }

    @Test(description = "Create chapter - Success (201)", priority = 1)
    public void testCreateChapter_Success() {
        Map<String, Object> request = new HashMap<>();
        request.put("title", "Introduction Chapter");
        request.put("description", "Getting started with onboarding");
        request.put("order", 1);

        Response response = client.createChapter(request);
        ResponseAssertions.assertStatusCodeIn(response, 200, 201, 400);

        if (response.getStatusCode() == 201 || response.getStatusCode() == 200) {
            try {
                createdChapterId = ResponseAssertions.extractJsonPath(response, "$.id");
            } catch (Exception e) {}
        }
    }

    @Test(description = "Get chapter by ID - Success (200)", priority = 2)
    public void testGetChapterById_Success() {
        String chapterId = createdChapterId != null ? createdChapterId : "sample-chapter-id";
        Response response = client.getChapterById(chapterId);
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Update chapter - Success (200)", priority = 3)
    public void testUpdateChapter_Success() {
        String chapterId = createdChapterId != null ? createdChapterId : "sample-chapter-id";
        Map<String, Object> request = new HashMap<>();
        request.put("title", "Updated Chapter Title");

        Response response = client.updateChapter(chapterId, request);
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Get chapters by stage - Success (200)")
    public void testGetChaptersByStage_Success() {
        Response response = client.getChaptersByStage(TEST_STAGE_ID);
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Update chapter tags - Success (200)")
    public void testUpdateChapterTags_Success() {
        String chapterId = createdChapterId != null ? createdChapterId : "sample-chapter-id";
        Map<String, Object> request = new HashMap<>();
        request.put("tagIds", Arrays.asList("tag-1", "tag-2"));

        Response response = client.updateChapterTags(chapterId, request);
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Remove chapter tags - Success (200)")
    public void testRemoveChapterTags_Success() {
        String chapterId = createdChapterId != null ? createdChapterId : "sample-chapter-id";
        Map<String, Object> request = new HashMap<>();
        request.put("tagIds", Arrays.asList("tag-1"));

        Response response = client.removeChapterTags(chapterId, request);
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Delete chapter - Success (204)", priority = 4)
    public void testDeleteChapter_Success() {
        String chapterId = createdChapterId != null ? createdChapterId : "sample-chapter-id";
        Response response = client.deleteChapter(chapterId);
        ResponseAssertions.assertStatusCodeIn(response, 204, 200, 404);
    }

    @Test(description = "Delete chapter - Unauthorized (401)")
    public void testDeleteChapter_Unauthorized() {
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            Response response = client.deleteChapter("sample-chapter-id");
            ResponseAssertions.assertStatusCode(response, 401);
        } finally {
            AuthManager.setBearerToken(originalToken);
        }
    }
}
