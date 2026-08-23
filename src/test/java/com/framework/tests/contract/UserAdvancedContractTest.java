package com.framework.tests.contract;

import com.framework.base.BaseTest;
import com.framework.endpoints.UserEndpoints;
import com.framework.models.request.CreateUserRequest;
import com.framework.utils.ContractValidator;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

/**
 * Advanced contract tests using the ContractValidator utility.
 * Validates headers, response time, field presence, types, and business rules.
 */
@Feature("User API Contract Tests - Advanced Validation")
public class UserAdvancedContractTest extends BaseTest {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Full contract validation for GET single user")
    public void testGetUserFullContract() {
        Response response = UserEndpoints.getUserById(2);

        ContractValidator.forResponse(response)
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectResponseTimeBelow(5000)
                .expectFieldPresent("data.id")
                .expectFieldPresent("data.email")
                .expectFieldPresent("data.first_name")
                .expectFieldPresent("data.last_name")
                .expectFieldPresent("data.avatar")
                .expectFieldPresent("support.url")
                .expectFieldPresent("support.text")
                .expectFieldType("data.id", Integer.class)
                .expectFieldType("data.email", String.class)
                .expectFieldType("data.first_name", String.class)
                .expectFieldType("data.last_name", String.class)
                .expectSchema("get-user-schema.json")
                .assertContract();
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Full contract validation for GET users list")
    public void testGetUsersListFullContract() {
        Response response = UserEndpoints.getUsers(1);

        ContractValidator.forResponse(response)
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectResponseTimeBelow(5000)
                .expectFieldPresent("page")
                .expectFieldPresent("per_page")
                .expectFieldPresent("total")
                .expectFieldPresent("total_pages")
                .expectFieldPresent("data")
                .expectFieldType("page", Integer.class)
                .expectFieldType("per_page", Integer.class)
                .expectFieldValue("page", 1)
                .expectListMinSize("data", 1)
                .expectListItemsHaveFields("data",
                        "id", "email", "first_name", "last_name", "avatar")
                .expectSchema("get-users-list-schema.json")
                .assertContract();
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Full contract validation for POST create user")
    public void testCreateUserFullContract() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("John Doe")
                .job("QA Engineer")
                .build();

        Response response = UserEndpoints.createUser(request);

        ContractValidator.forResponse(response)
                .expectStatusCode(201)
                .expectContentType(ContentType.JSON)
                .expectResponseTimeBelow(5000)
                .expectFieldPresent("id")
                .expectFieldPresent("name")
                .expectFieldPresent("job")
                .expectFieldPresent("createdAt")
                .expectFieldValue("name", "John Doe")
                .expectFieldValue("job", "QA Engineer")
                .expectFieldType("id", String.class)
                .expectFieldType("createdAt", String.class)
                .expectSchema("create-user-schema.json")
                .assertContract();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Full contract validation for PUT update user")
    public void testUpdateUserFullContract() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Jane Updated")
                .job("Senior Engineer")
                .build();

        Response response = UserEndpoints.updateUser(2, request);

        ContractValidator.forResponse(response)
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectFieldPresent("name")
                .expectFieldPresent("job")
                .expectFieldPresent("updatedAt")
                .expectFieldValue("name", "Jane Updated")
                .expectFieldValue("job", "Senior Engineer")
                .expectSchema("update-user-schema.json")
                .assertContract();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Contract validation for DELETE user")
    public void testDeleteUserContract() {
        Response response = UserEndpoints.deleteUser(2);

        ContractValidator.forResponse(response)
                .expectStatusCode(204)
                .expectResponseTimeBelow(5000)
                .assertContract();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Contract validation for non-existent user returns empty body")
    public void testNotFoundUserContract() {
        Response response = UserEndpoints.getUserById(999);

        ContractValidator.forResponse(response)
                .expectStatusCode(404)
                .expectContentType(ContentType.JSON)
                .assertContract();
    }

    public void placeholderMEthod(){
        System.out.println("harry branch");
        System.out.println("==THis is placeholder method1122===");
        System.out.println("==This is main branch===");
    }
}
