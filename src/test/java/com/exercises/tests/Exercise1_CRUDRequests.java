package com.exercises.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Exercise1_CRUDRequests {
    private static final String BASE_URL = "https://api.restful-api.dev";

    @Test
    public void getObjectById_ShouldReturnOK() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .basePath("/objects/7")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().response();

        assertThat(response.jsonPath().getString("id"), is("7"));
        assertThat(response.jsonPath().getString("name"), is("Apple MacBook Pro 16"));
    }
}
