# Multi-User Type Authentication - Quick Start

## ✅ What Was Implemented

Your framework now supports **3 user types** with separate bearer tokens:
- **ADMIN** - Full administrative access
- **EDITOR** - Content editing permissions  
- **VIEWER** - Read-only access

## 📁 Files Created/Modified

### Created:
1. `src/test/java/config/UserType.java` - Enum defining user types
2. `src/test/java/tests/examples/UserTypeExampleTest.java` - Example tests
3. `USER_TYPE_AUTHENTICATION_GUIDE.md` - Comprehensive documentation

### Modified:
1. `src/test/java/config/AuthManager.java` - Added user type support
2. `src/test/java/config/RequestSpecFactory.java` - Added user type methods
3. `src/test/java/config/EnvironmentConfig.java` - Added token loading per user type
4. `src/test/resources/config.properties` - Added tokens for each user type

## 🚀 Quick Usage

### Option 1: Set User Type Globally
```java
@Test
public void testAsAdmin() {
    AuthManager.setUserType(UserType.ADMIN);
    Response response = client.getPreferredJourney();
    ResponseAssertions.assertStatusCodeIn(response, 200);
}
```

### Option 2: Per-Request User Type
```java
@Test
public void testMultipleUsers() {
    Response adminResp = given()
        .spec(RequestSpecFactory.getRequestSpec(UserType.ADMIN))
        .when()
        .get("/api/v1/users/preferred-journey");
    
    Response viewerResp = given()
        .spec(RequestSpecFactory.getRequestSpec(UserType.VIEWER))
        .when()
        .get("/api/v1/users/preferred-journey");
}
```

## ⚙️ Configuration

Update `src/test/resources/config.properties`:

```properties
# Replace these with your actual tokens
admin.bearerToken=YOUR_ADMIN_TOKEN_HERE
editor.bearerToken=YOUR_EDITOR_TOKEN_HERE
viewer.bearerToken=YOUR_VIEWER_TOKEN_HERE
```

## 🎯 Key Features

1. **Backward Compatible** - Existing tests work without changes
2. **Thread-Safe** - Uses ThreadLocal for parallel execution
3. **Flexible** - Use globally or per-request
4. **Easy Testing** - Test authorization for different user types
5. **Clean API** - Simple, intuitive methods

## 📚 Documentation

For detailed examples and API reference, see:
- `USER_TYPE_AUTHENTICATION_GUIDE.md` - Full documentation
- `tests/examples/UserTypeExampleTest.java` - Working examples

## 🧪 Test Your Setup

Run the example test:
```bash
mvn test -Dtest=UserTypeExampleTest
```

## 💡 Common Patterns

### Test Authorization
```java
@Test
public void testViewerCannotDelete() {
    AuthManager.setUserType(UserType.VIEWER);
    Response response = client.deleteUser(userId);
    ResponseAssertions.assertStatusCodeIn(response, 403);
}
```

### Data Provider for Multiple Users
```java
@DataProvider(name = "users")
public Object[][] users() {
    return new Object[][]{{UserType.ADMIN}, {UserType.EDITOR}, {UserType.VIEWER}};
}

@Test(dataProvider = "users")
public void testAllUsers(UserType userType) {
    AuthManager.setUserType(userType);
    // test logic
}
```

### Reset After Test
```java
@AfterMethod
public void cleanup() {
    AuthManager.resetToDefault();
}
```

## ✨ Benefits

- **Authorization Testing** - Easily test access control
- **Role-Based Tests** - Run same test with different permissions
- **Cleaner Tests** - No manual token management
- **Maintainable** - Tokens centralized in config
- **Scalable** - Easy to add more user types

## 🔧 Troubleshooting

| Issue | Solution |
|-------|----------|
| 401 Unauthorized | Check token in config.properties |
| Wrong user active | Call `AuthManager.resetToDefault()` |
| Token not loading | Verify property key format: `{role}.bearerToken` |

---

**Ready to use!** Replace placeholder tokens in `config.properties` and start testing! 🎉
