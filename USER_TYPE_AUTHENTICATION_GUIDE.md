# Multi-User Type Authentication Guide

This guide explains how to use the multi-user type authentication feature in the API automation framework.

## Overview

The framework supports three user types:
- **ADMIN** - Full access to all endpoints
- **EDITOR** - Can modify content but with limited administrative access
- **VIEWER** - Read-only access

Each user type has its own bearer token configured in `config.properties`.

## Configuration

### Step 1: Configure Tokens in config.properties

Update `src/test/resources/config.properties` with tokens for each user type:

```properties
# Default bearer token (backward compatibility)
bearerToken=YOUR_DEFAULT_TOKEN

# Admin user token
admin.bearerToken=YOUR_ADMIN_TOKEN_HERE

# Editor user token
editor.bearerToken=YOUR_EDITOR_TOKEN_HERE

# Viewer user token
viewer.bearerToken=YOUR_VIEWER_TOKEN_HERE
```

## Usage Examples

### Method 1: Using AuthManager to Set User Type

Set the user type globally for all subsequent requests in the test:

```java
@Test
public void testAsAdmin() {
    // Set user type to Admin
    AuthManager.setUserType(UserType.ADMIN);
    
    // All requests will now use Admin token
    Response response = client.getPreferredJourney();
    
    // Assert admin can access
    ResponseAssertions.assertStatusCodeIn(response, 200);
}

@Test
public void testAsEditor() {
    // Set user type to Editor
    AuthManager.setUserType(UserType.EDITOR);
    
    // All requests will now use Editor token
    Response response = client.updatePreferredJourney(requestBody);
    
    ResponseAssertions.assertStatusCodeIn(response, 200);
}

@Test
public void testAsViewer() {
    // Set user type to Viewer
    AuthManager.setUserType(UserType.VIEWER);
    
    // All requests will now use Viewer token
    Response response = client.getPreferredJourney();
    
    ResponseAssertions.assertStatusCodeIn(response, 200);
}
```

### Method 2: Using RequestSpecFactory with User Type

Pass the user type directly to `RequestSpecFactory` without changing the global state:

```java
@Test
public void testMultipleUsersInSameTest() {
    // Test with Admin
    Response adminResponse = given()
            .spec(RequestSpecFactory.getRequestSpec(UserType.ADMIN))
            .when()
            .get("/api/v1/users/preferred-journey");
    
    // Test with Editor in the same test
    Response editorResponse = given()
            .spec(RequestSpecFactory.getRequestSpec(UserType.EDITOR))
            .when()
            .get("/api/v1/users/preferred-journey");
    
    // Test with Viewer
    Response viewerResponse = given()
            .spec(RequestSpecFactory.getRequestSpec(UserType.VIEWER))
            .when()
            .get("/api/v1/users/preferred-journey");
    
    // Assert different behaviors
    Assert.assertEquals(adminResponse.getStatusCode(), 200);
    Assert.assertEquals(editorResponse.getStatusCode(), 200);
    Assert.assertEquals(viewerResponse.getStatusCode(), 200);
}
```

### Method 3: Using Custom Client Methods

If your client needs to support different user types:

```java
// In your client class
public Response getPreferredJourney(UserType userType) {
    return given()
            .spec(RequestSpecFactory.getRequestSpec(userType))
            .when()
            .get(BASE_PATH + "/users/preferred-journey");
}

// In your test
@Test
public void testWithClientMethod() {
    Response response = client.getPreferredJourney(UserType.ADMIN);
    ResponseAssertions.assertStatusCodeIn(response, 200);
}
```

## Authorization Testing

Test that different users have appropriate access:

```java
@Test
public void testAdminCanCreateUser() {
    AuthManager.setUserType(UserType.ADMIN);
    
    Response response = client.createUser(userData);
    
    // Admin should be able to create users
    ResponseAssertions.assertStatusCodeIn(response, 201);
}

@Test
public void testViewerCannotCreateUser() {
    AuthManager.setUserType(UserType.VIEWER);
    
    Response response = client.createUser(userData);
    
    // Viewer should get 403 Forbidden
    ResponseAssertions.assertStatusCodeIn(response, 403);
}

@Test
public void testEditorCanUpdateButNotDelete() {
    AuthManager.setUserType(UserType.EDITOR);
    
    // Editor can update
    Response updateResponse = client.updateUser(userId, updateData);
    ResponseAssertions.assertStatusCodeIn(updateResponse, 200);
    
    // But cannot delete
    Response deleteResponse = client.deleteUser(userId);
    ResponseAssertions.assertStatusCodeIn(deleteResponse, 403);
}
```

## Best Practices

### 1. Reset Token After Each Test

Use `@AfterMethod` to reset to default state:

```java
@AfterMethod
public void cleanup() {
    AuthManager.resetToDefault();
}
```

### 2. Check Current User Type

```java
UserType currentUser = AuthManager.getCurrentUserType();
if (currentUser == UserType.ADMIN) {
    // Run admin-specific assertions
}
```

### 3. Use Data Providers for Multiple User Testing

```java
@DataProvider(name = "userTypes")
public Object[][] userTypes() {
    return new Object[][]{
        {UserType.ADMIN, 200},
        {UserType.EDITOR, 200},
        {UserType.VIEWER, 403}
    };
}

@Test(dataProvider = "userTypes")
public void testAccessControl(UserType userType, int expectedStatus) {
    AuthManager.setUserType(userType);
    
    Response response = client.deleteUser(userId);
    
    ResponseAssertions.assertStatusCodeIn(response, expectedStatus);
}
```

### 4. Override with Custom Token

You can still use custom tokens when needed:

```java
@Test
public void testWithExpiredToken() {
    AuthManager.setBearerToken("expired-token");
    
    Response response = client.getPreferredJourney();
    
    // Should get 401 Unauthorized
    ResponseAssertions.assertStatusCodeIn(response, 401);
}
```

### 5. Test Without Authentication

```java
@Test
public void testWithoutAuth() {
    Response response = given()
            .spec(RequestSpecFactory.getRequestSpecWithoutAuth())
            .when()
            .get("/api/v1/users/preferred-journey");
    
    // Should get 401 Unauthorized
    ResponseAssertions.assertStatusCodeIn(response, 401);
}
```

## API Reference

### AuthManager

| Method | Description |
|--------|-------------|
| `setUserType(UserType)` | Set the current user type and load their token |
| `getCurrentUserType()` | Get the current user type |
| `getBearerToken()` | Get the current bearer token |
| `getBearerToken(UserType)` | Get token for a specific user type |
| `setBearerToken(String)` | Set a custom bearer token |
| `clearBearerToken()` | Clear the bearer token |
| `resetToDefault()` | Reset to the default token |
| `hasToken()` | Check if a token is set |

### RequestSpecFactory

| Method | Description |
|--------|-------------|
| `getRequestSpec()` | Get request spec with current user's token |
| `getRequestSpec(UserType)` | Get request spec with specific user's token |
| `getRequestSpecWithoutAuth()` | Get request spec without authentication |
| `getRequestSpecWithHeaders(String, String)` | Get request spec with custom headers |
| `getRequestSpecWithHeaders(UserType, String, String)` | Get request spec for user with custom headers |

### UserType Enum

| Value | Description |
|-------|-------------|
| `UserType.ADMIN` | Administrator with full access |
| `UserType.EDITOR` | Editor with modify permissions |
| `UserType.VIEWER` | Read-only access |

## Example Test Class

```java
package tests.user;

import config.AuthManager;
import config.UserType;
import client.UserManagementClient;
import assertions.ResponseAssertions;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserAuthorizationTest {
    private UserManagementClient client;

    @BeforeClass
    public void setup() {
        client = new UserManagementClient();
    }

    @AfterMethod
    public void cleanup() {
        AuthManager.resetToDefault();
    }

    @Test(description = "Admin can access all user data")
    public void testAdminAccess() {
        AuthManager.setUserType(UserType.ADMIN);
        
        Response response = client.getAllNewHires(0, 10);
        
        ResponseAssertions.assertStatusCodeIn(response, 200);
    }

    @Test(description = "Editor has limited access")
    public void testEditorAccess() {
        AuthManager.setUserType(UserType.EDITOR);
        
        Response response = client.getAllNewHires(0, 10);
        
        ResponseAssertions.assertStatusCodeIn(response, 200, 403);
    }

    @Test(description = "Viewer can only read")
    public void testViewerAccess() {
        AuthManager.setUserType(UserType.VIEWER);
        
        // Can read
        Response getResponse = client.getPreferredJourney();
        ResponseAssertions.assertStatusCodeIn(getResponse, 200);
        
        // Cannot write
        Response updateResponse = client.updatePreferredJourney(requestBody);
        ResponseAssertions.assertStatusCodeIn(updateResponse, 403);
    }
}
```

## Troubleshooting

### Token Not Working
- Verify the token is correctly configured in `config.properties`
- Check if the token has expired
- Ensure the token format is correct (JWT format)

### Getting 401 Unauthorized
- Check if `AuthManager.hasToken()` returns true
- Verify the token is being included in the Authorization header
- Check if the token matches the expected format

### Wrong User Type
- Use `AuthManager.getCurrentUserType()` to check the current user
- Call `AuthManager.resetToDefault()` to reset to default state
- Ensure `@AfterMethod` cleanup is running

## Migration from Old Code

If you have existing tests, they will continue to work with the default token. To migrate:

**Before:**
```java
@Test
public void testSomething() {
    Response response = client.getPreferredJourney();
    ResponseAssertions.assertStatusCodeIn(response, 200);
}
```

**After (with user type):**
```java
@Test
public void testSomething() {
    AuthManager.setUserType(UserType.ADMIN);
    Response response = client.getPreferredJourney();
    ResponseAssertions.assertStatusCodeIn(response, 200);
}
```

No changes are required to existing client methods or assertions.
