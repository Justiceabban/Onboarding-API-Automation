package client;

import config.*;
import io.restassured.response.*;

import static io.restassured.RestAssured.*;

/**
 * Client for User Management endpoints.
 * Handles user profile operations, preferred journey management, and user CRUD operations.
 */
public class UserManagementClient {
    private static final String BASE_PATH = "/api/v1";

    /**
     * Get new hire preferred journey.
     * GET /api/v1/users/preferred-journey
     * @return Response with preferred journey details
     */
    public Response getPreferredJourney() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .get(BASE_PATH + "/users/preferred-journey");
    }

    /**
     * Update new hire preferred journey.
     * PUT /api/v1/users/preferred-journey
     * @param requestBody PreferredJourneyRequest object
     * @return Response with generic message
     */
    public Response updatePreferredJourney(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .put(BASE_PATH + "/users/preferred-journey");
    }

    /**
     * Get all new hires (Storyblok integration).
     * GET /api/v1/storyblok/user/new_hires
     * @param page Page number (zero-based)
     * @param size Number of items per page
     * @return Response with list of new hires
     */
    public Response getAllNewHires(int page, int size) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(BASE_PATH + "/storyblok/user/new_hires");
    }

    /**
     * Get all new hires with default pagination.
     * @return Response with list of new hires
     */
    public Response getAllNewHires() {
        return getAllNewHires(0, 100);
    }

    /**
     * Get all users with pagination and filtering.
     * GET /api/v1/users
     * @param page Page number
     * @param size Page size
     * @param search Search term
     * @param role Filter by role
     * @param status Filter by status
     * @return Response with paginated users list
     */
    public Response getAllUsers(int page, int size, String search, String role, String status) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("search", search)
                .queryParam("role", role)
                .queryParam("status", status)
                .when()
                .get(BASE_PATH + "/users");
    }

    /**
     * Get all users with default parameters.
     * @return Response with users list
     */
    public Response getAllUsers() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .get(BASE_PATH + "/users");
    }

    public Response getAllUsersWithoutToken() {
        return given()
                .spec(RequestSpecFactory.getRequestSpecWithoutAuth())
                .when()
                .get(BASE_PATH + "/users");
    }

    /**
     * Get user by email.
     * GET /api/v1/users?search={email}
     * @param email User email
     * @return Response with user details
     */

    public Response getUserByEmail(String email) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("search", email)
                .when()
                .get(BASE_PATH + "/users");
    }

    /**
     * Create a new user.
     * POST /api/v1/users
     * @param requestBody User creation request
     * @return Response with generic message
     */
    public Response createUser(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/users");
    }

    /**
     * Verify user email.
     * POST /api/v1/users/verify-email
     * @param requestBody Email verification request
     * @return Response with generic message
     */
    public Response verifyEmail(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getRequestSpecWithoutAuth())
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/users/verify-email");
    }

    public Response verifyEmailWithInvalidToken(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getRequestSpecWithoutAuth())
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/users/verify-email");
    }

    /**
     * Get user by ID.
     * GET /api/v1/users/{userId}
     * @param userId User ID
     * @return Response with user details
     */
    public Response getUserById(String userId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("userId", userId)
                .when()
                .get(BASE_PATH + "/users/{userId}");
    }

    /**
     * Update user information.
     * PATCH /api/v1/users/{userId}
     * @param userId User ID
     * @param requestBody User update request
     * @return Response with generic message
     */
    public Response updateUser(String userId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("userId", userId)
                .body(requestBody)
                .when()
                .patch(BASE_PATH + "/users/{userId}");
    }

    /**
     * Delete user.
     * DELETE /api/v1/users/{userId}
     * @param userId User ID
     * @return Response
     */
    public Response deleteUser(String userId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("userId", userId)
                .when()
                .delete(BASE_PATH + "/users/{userId}");
    }

    /**
     * Archive or unarchive a user.
     * PATCH /api/v1/users/archive
     * @param requestBody Archive user request
     * @return Response with generic message
     */
    public Response archiveUser(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .patch(BASE_PATH + "/users/archive");
    }

    /**
     * Get user profile.
     * GET /api/v1/profile
     * @return Response with user profile
     */
    public Response getProfile() {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .when()
                .get(BASE_PATH + "/profile");
    }

    /**
     * Update user profile.
     * PATCH /api/v1/profile
     * @param requestBody Profile update request
     * @return Response with updated profile
     */
    public Response updateProfile(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .patch(BASE_PATH + "/profile");
    }

    /**
     * Update user profile with multipart form data.
     * PATCH /api/v1/profile
     * @param firstName First name
     * @param lastName Last name
     * @param preferredLanguage Preferred language
     * @return Response with updated profile
     */
    public Response updateProfileMultipart(String firstName, String lastName, String preferredLanguage) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .contentType("multipart/form-data")
                .multiPart("firstName", firstName)
                .multiPart("lastName", lastName)
                .multiPart("preferredLanguage", preferredLanguage)
                .when()
                .patch(BASE_PATH + "/profile");
    }

    public Response updateProfileMultipartInvalidData(){
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .contentType("multipart/form-data")
                .multiPart("invalidField", "invalidValue")
                .when()
                .patch(BASE_PATH + "/profile");
    }

    /**
     * Get all open invites.
     * GET /api/v1/invitation/open-invite
     * @param page Page number
     * @param size Page size
     * @param search Search term
     * @return Response with open invites
     */
    public Response getAllOpenInvites(int page, int size, String search) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("search", search)
                .when()
                .get(BASE_PATH + "/invitation/open-invite");
    }

    /**
     * Get all open invites with defaults.
     * @return Response with open invites
     */
    public Response getAllOpenInvites() {
        return getAllOpenInvites(0, 10, "");
    }

    /**
     * Create open invite.
     * POST /api/v1/invitation/open-invite
     * @param requestBody Open invite request
     * @return Response with generic message
     */
    public Response createOpenInvite(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/invitation/open-invite");
    }

    public Response getOpenInviteByCode(String code) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .queryParam("search", code)
                .when()
                .get(BASE_PATH + "/invitation/open-invite");
    }

    /**
     * Verify invitation code.
     * POST /api/v1/invitation/open-invite/verify
     * @param requestBody Code verification request
     * @return Response with invitation code details
     */
    public Response verifyInvitationCode(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/invitation/open-invite/verify");
    }

    /**
     * Register with open invite for new users.
     * POST /api/v1/invitation/open-invite/register
     * @param requestBody Registration request
     * @return Response with generic message
     */
    public Response registerWithOpenInviteForNewUsers(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/invitation/open-invite/register");
    }

    /**
     * Register with open invite for existing users.
     * POST /api/v1/invitation/open-invite/existing_users
     * @param requestBody Code request
     * @return Response with generic message
     */
    public Response registerWithOpenInviteForExistingUsers(Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getViewerRequestSpec())
                .body(requestBody)
                .when()
                .post(BASE_PATH + "/invitation/open-invite/existing_users");
    }

    /**
     * Delete open invite.
     * DELETE /api/v1/invitation/open-invite/{codeId}
     * @param codeId Code ID
     * @return Response
     */
    public Response deleteOpenInvite(String codeId) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("codeId", codeId)
                .when()
                .delete(BASE_PATH + "/invitation/open-invite/{codeId}");
    }

    /**
     * Update open invite.
     * PATCH /api/v1/invitation/open-invite/{codeId}
     * @param codeId Code ID
     * @param requestBody Update request
     * @return Response with generic message
     */
    public Response updateOpenInvite(String codeId, Object requestBody) {
        return given()
                .spec(RequestSpecFactory.getAdminRequestSpec())
                .pathParam("codeId", codeId)
                .body(requestBody)
                .when()
                .patch(BASE_PATH + "/invitation/open-invite/{codeId}");
    }
}
