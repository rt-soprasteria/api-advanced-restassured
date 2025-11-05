package com.exercises.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Exercise2_NegativeTests {
    private static final String BASE_URL = "https://api.restful-api.dev";

    @Test
    public void getObjectByInvalidId_ShouldReturnNotFound() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .basePath("/objects/70")
                .when()
                .get()
                .then()
                .statusCode(404)
                .extract().response();
    }

    @Test
    public void postObjectInvalidBody_ShouldReturnBadRequest() {
        String requestBody = "";

        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .basePath("/objects")
                .body(requestBody)
                .header("Content-Type", "application/json")
                .when()
                .post()
                .then()
                .statusCode(400)
                .extract().response();
    }
}
