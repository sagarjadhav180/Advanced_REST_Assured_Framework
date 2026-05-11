package com.framework.tests.contract;

import com.framework.base.BaseTest;
import com.framework.endpoints.UserEndpoints;
import com.framework.models.request.CreateUserRequest;
import com.framework.utils.SchemaValidator;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.annotations.Test;

/**
 * Contract tests using JSON Schema validation.
 * Validates that API responses conform to predefined JSON schemas.
 */
@Feature("User API Contract Tests - Schema Validation")
public class UserSchemaContractTest extends BaseTest {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validate GET single user response matches schema contract")
    public void testGetUserSchemaContract() {
        Response response = UserEndpoints.getUserById(2);

        response.then()
                .statusCode(200)
                .body(SchemaValidator.matchesSchema("get-user-schema.json"));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validate GET users list response matches schema contract")
    public void testGetUsersListSchemaContract() {
        Response response = UserEndpoints.getUsers(1);

        response.then()
                .statusCode(200)
                .body(SchemaValidator.matchesSchema("get-users-list-schema.json"));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validate POST create user response matches schema contract")
    public void testCreateUserSchemaContract() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("John Doe")
                .job("QA Engineer")
                .build();

        Response response = UserEndpoints.createUser(request);

        response.then()
                .statusCode(201)
                .body(SchemaValidator.matchesSchema("create-user-schema.json"));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validate PUT update user response matches schema contract")
    public void testUpdateUserSchemaContract() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Jane Doe")
                .job("Lead Engineer")
                .build();

        Response response = UserEndpoints.updateUser(2, request);

        response.then()
                .statusCode(200)
                .body(SchemaValidator.matchesSchema("update-user-schema.json"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate PATCH user response matches schema contract")
    public void testPatchUserSchemaContract() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Updated Name")
                .job("Updated Job")
                .build();

        Response response = UserEndpoints.patchUser(2, request);

        response.then()
                .statusCode(200)
                .body(SchemaValidator.matchesSchema("update-user-schema.json"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate DELETE user returns 204 No Content")
    public void testDeleteUserContract() {
        Response response = UserEndpoints.deleteUser(2);

        response.then()
                .statusCode(204);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate GET non-existent user returns 404")
    public void testGetNonExistentUserContract() {
        Response response = UserEndpoints.getUserById(999);

        response.then()
                .statusCode(404);
    }
}
