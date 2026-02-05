# Onboarding API Automation Framework
## Overview
Enterprise-grade API automation framework for the **Onboarding Backend API** built with **Java**, **Rest Assured**, and **TestNG**. This framework follows industry best practices for maintainability, scalability, and clean code architecture.
## 🏗️ Architecture
The framework follows a **modular design** with clear separation of concerns:
```
src/test/java/
├── config/               # Configuration management
│   ├── EnvironmentConfig.java
│   ├── AuthManager.java
│   └── RequestSpecFactory.java
├── client/               # API client classes (request logic only)
│   ├── UserManagementClient.java
│   ├── NotificationClient.java
│   ├── JourneyClient.java
│   ├── StageClient.java
│   ├── PageClient.java
│   └── DataManagementClient.java
├── models/               # POJO models
│   ├── request/          # Request DTOs
│   └── response/         # Response DTOs
├── assertions/           # Reusable assertion utilities
│   └── ResponseAssertions.java
├── tests/                # Test classes
│   ├── user/
│   ├── notification/
│   ├── journey/
│   └── data/
└── utils/                # Utility classes
    └── JsonUtils.java
```
## 🎯 Key Features
- ✅ **Clean Architecture**: Separation of test logic from request logic
- ✅ **SOLID Principles**: Maintainable and extensible code
- ✅ **Multi-Environment Support**: Dev, Test, Stage
- ✅ **Bearer Token Authentication**: Configurable and thread-safe
- ✅ **Builder Pattern**: Fluent request model construction
- ✅ **Comprehensive Assertions**: Status codes, headers, JSON paths, response times
- ✅ **Detailed Logging**: SLF4J + Logback for debugging
- ✅ **Parallel Execution**: TestNG parallel test execution
- ✅ **No Hardcoded Values**: Everything configurable
## 📋 Prerequisites
- **Java 11** or higher
- **Maven 3.6+**
- Valid JWT bearer token for authentication
## 🚀 Getting Started
### 1. Configure Environment
Edit `src/test/resources/config.properties`:
```properties
environment=dev
bearerToken=your-jwt-token-here
```
### 2. Run Tests
**Run all tests:**
```bash
mvn clean test
```
**Run specific test suite:**
```bash
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testng.xml
```
**Run with specific environment:**
```bash
mvn clean test -Denvironment=stage
```
**Run with Maven profiles:**
```bash
# Dev environment (default)
mvn clean test -Pdev
# Test environment
mvn clean test -Ptest
# Stage environment
mvn clean test -Pstage
```
### 3. View Reports
TestNG reports are generated in:
```
target/surefire-reports/index.html
```
Logs are available in:
```
logs/test-execution.log
```
## 📁 Project Structure Details
### Config Package
**EnvironmentConfig.java**
- Loads configuration from properties file
- Manages environment-specific URLs
- Supports system property overrides
**AuthManager.java**
- Thread-safe bearer token management
- Dynamic token injection
- Support for tests with/without authentication
**RequestSpecFactory.java**
- Reusable Rest Assured request specifications
- Automatic authentication header injection
- Common headers and logging configuration
### Client Package
Each client class represents a logical grouping of API endpoints:
- **UserManagementClient**: User profile and preferred journey operations
- **NotificationClient**: Notification settings management
- **JourneyClient**: Journey CRUD operations
- **StageClient**: Stage management and reordering
- **PageClient**: Page retrieval and updates
- **DataManagementClient**: Data record CRUD operations
**Client Design Principles:**
- ✅ **No Assertions**: Clients only make requests, never assert
- ✅ **Return Response**: Always return `io.restassured.response.Response`
- ✅ **Use RequestSpecFactory**: Reuse common request configuration
- ✅ **Clear Method Names**: Self-documenting method signatures
### Models Package
**Request Models** (models/request/):
- Builder pattern for fluent construction
- Jackson annotations for JSON mapping
- Validation-ready (optional)
**Response Models** (models/response/):
- POJOs matching API response schemas
- Jackson annotations for deserialization
- ToString methods for debugging
### Assertions Package
**ResponseAssertions.java** provides:
- Status code assertions
- Header validations
- Response time checks
- JSON path validations
- Array/collection assertions
- Model-specific assertions (ProblemDetail, GenericMessage)
- Comprehensive error messages with response bodies
### Tests Package
**Test Design Principles:**
- ✅ **Arrange-Act-Assert** pattern
- ✅ **Clear test descriptions**
- ✅ **Meaningful test names**
- ✅ **Proper cleanup** (AuthManager reset)
- ✅ **Test isolation**
- ✅ **Reusable client instances**
## 🧪 Writing New Tests
### Example Test Structure
```java
@Test(description = "Get user profile - Success scenario (200)")
public void testGetUserProfile_Success() {
    // Arrange
    String userId = "test-user-123";
    // Act
    Response response = userClient.getUserById(userId);
    // Assert
    ResponseAssertions.assertStatusCode(response, 200);
    ResponseAssertions.assertContentTypeJson(response);
    ResponseAssertions.assertJsonPathExists(response, "$.userId");
    ResponseAssertions.assertResponseTimeBelow(response, 3000);
}
```
## 🔧 Extending the Framework
### Adding a New Client
1. Create new client class in `client/` package
2. Extend with methods following the pattern:
```java
public Response getResource(String id) {
    return given()
            .spec(RequestSpecFactory.getRequestSpec())
            .pathParam("id", id)
            .when()
            .get(BASE_PATH + "/resource/{id}");
}
```
### Adding New Models
1. Create POJO in `models/request/` or `models/response/`
2. Add Jackson annotations
3. Implement builder pattern (optional but recommended)
### Adding New Tests
1. Create test class in appropriate `tests/` subdirectory
2. Follow Arrange-Act-Assert pattern
3. Use descriptive test names and descriptions
4. Add to `testng.xml` if needed
## 🌍 Environment Configuration
**Available Environments:**
| Environment | Base URL |
|------------|----------|
| dev | https://api.onboarding.test.gcw.ng.telekom.net |
| test | https://api.onboardingv2.test.gcw.ng.telekom.net |
| stage | https://api.onboarding.stage.test.gcw.ng.telekom.net |
Switch environments by updating `config.properties` or using system properties:
```bash
mvn test -Denvironment=stage
```
## 📊 Logging
The framework uses **SLF4J** with **Logback** for comprehensive logging:
- Console output for real-time feedback
- File logging with daily rotation
- Configurable log levels per package
- Request/response logging for debugging
Configure logging in `src/test/resources/logback-test.xml`
## 🔐 Authentication
The framework supports Bearer token authentication:
1. **Configure in properties file**:
```properties
bearerToken=your-jwt-token-here
```
2. **Override in tests**:
```java
AuthManager.setBearerToken("custom-token");
// Run tests
AuthManager.clearBearerToken(); // Cleanup
```
3. **Tests without authentication**:
```java
AuthManager.clearBearerToken();
Response response = client.publicEndpoint();
```
## 🧩 Best Practices
### ✅ DO:
- Use client classes for all API requests
- Follow Arrange-Act-Assert pattern
- Add meaningful test descriptions
- Use builder pattern for request models
- Clean up test data in teardown methods
- Reset AuthManager after token manipulation
- Use ResponseAssertions for all validations
### ❌ DON'T:
- Put assertions in client methods
- Hardcode URLs or tokens
- Duplicate request setup logic
- Mix test logic with request logic
- Leave test data in the system
- Share state between tests
## 📝 Maintenance
### Regular Tasks:
1. Update dependencies in `pom.xml` quarterly
2. Review and update bearer tokens as they expire
3. Add new endpoints as API evolves
4. Refactor tests for better coverage
5. Monitor and optimize slow tests
## 🤝 Contributing
When adding new features:
1. Follow existing package structure
2. Maintain code style consistency
3. Add JavaDoc comments to public methods
4. Write comprehensive tests
5. Update this README if needed
## 📞 Support
For questions or issues:
- Check API documentation
- Review test logs in `logs/` directory
- Enable DEBUG logging for detailed troubleshooting
## 📄 License
Apache 2.0 License
---
**Built with ❤️ following enterprise best practices**#   O n b o a r d i n g - A P I - A u t o m a t i o n  
 