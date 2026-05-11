package com.framework.endpoints;

import com.framework.models.request.CreateUserRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Encapsulates all User API endpoints.
 */
public class UserEndpoints {

    private static final String USERS = "/users";
    private static final String USER_BY_ID = "/users/{id}";

    public static Response getUsers(int page) {
        return given()
                .queryParam("page", page)
            .when()
                .get(USERS);
    }

    public static Response getUserById(int id) {
        return given()
                .pathParam("id", id)
            .when()
                .get(USER_BY_ID);
    }

    public static Response createUser(CreateUserRequest request) {
        return given()
                .body(request)
            .when()
                .post(USERS);
    }

    public static Response updateUser(int id, CreateUserRequest request) {
        return given()
                .pathParam("id", id)
                .body(request)
            .when()
                .put(USER_BY_ID);
    }

    public static Response patchUser(int id, CreateUserRequest request) {
        return given()
                .pathParam("id", id)
                .body(request)
            .when()
                .patch(USER_BY_ID);
    }

    public static Response deleteUser(int id) {
        return given()
                .pathParam("id", id)
            .when()
                .delete(USER_BY_ID);
    }
}
