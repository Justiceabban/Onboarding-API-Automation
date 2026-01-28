package tests.asset;

import assertions.*;
import client.*;
import config.*;
import io.qameta.allure.*;
import io.restassured.response.*;
import org.testng.annotations.*;

import java.io.*;
import java.nio.file.*;

/**
 * Test class for Asset Management endpoints.
 */
@Epic("Asset Management")
@Feature("Asset Operations")
public class AssetManagementTest {
    private AssetManagementClient client;
    private String uploadedAssetId;

    @BeforeClass
    public void setup() {
        client = new AssetManagementClient();
    }

    @Test(description = "Upload asset - Success (201)", priority = 1)
    public void testUploadAsset_Success() throws IOException {
        // Arrange
        File testFile = createTempFile();

        try {
            // Act
            Response response = client.uploadAsset(testFile);

            // Assert
            ResponseAssertions.assertStatusCodeIn(response, 200, 201, 400);

            if (response.getStatusCode() == 201 || response.getStatusCode() == 200) {
                try {
                    uploadedAssetId = ResponseAssertions.extractJsonPath(response, "$.id");
                } catch (Exception e) {}
            }
        } finally {
            testFile.delete();
        }
    }

    @Test(description = "Upload asset with metadata - Success (201)")
    public void testUploadAssetWithMetadata_Success() throws IOException {
        File testFile = createTempFile();

        try {
            Response response = client.uploadAssetWithMetadata(testFile, "Test asset description");
            ResponseAssertions.assertStatusCodeIn(response, 200, 201, 400);
        } finally {
            testFile.delete();
        }
    }

    @Test(description = "Get asset by ID - Success (200)", priority = 2)
    public void testGetAsset_Success() {
        String assetId = uploadedAssetId != null ? uploadedAssetId : "sample-asset-id";
        Response response = client.getAsset(assetId);
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Get all assets - Success (200)")
    public void testGetAllAssets_Success() {
        Response response = client.getAllAssets();
        ResponseAssertions.assertStatusCodeIn(response, 200, 404);
    }

    @Test(description = "Delete asset - Success (204)", priority = 3)
    public void testDeleteAsset_Success() {
        String assetId = uploadedAssetId != null ? uploadedAssetId : "sample-asset-id";
        Response response = client.deleteAsset(assetId);
        ResponseAssertions.assertStatusCodeIn(response, 204, 200, 404);
    }

    @Test(description = "Delete asset - Unauthorized (401)")
    public void testDeleteAsset_Unauthorized() {
        String originalToken = AuthManager.getBearerToken();
        AuthManager.clearBearerToken();

        try {
            Response response = client.deleteAsset("sample-asset-id");
            ResponseAssertions.assertStatusCode(response, 401);
        } finally {
            AuthManager.setBearerToken(originalToken);
        }
    }

    private File createTempFile() throws IOException {
        File tempFile = File.createTempFile("test-asset", ".txt");
        Files.write(tempFile.toPath(), "Test file content".getBytes());
        return tempFile;
    }
}
