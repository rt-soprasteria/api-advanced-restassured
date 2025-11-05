package com.examples.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ShadyMeadowsSingleApiRequests {

    private static final String BASE_URL = "https://automationintesting.online";

    @Test
    public void getAllRooms_ShouldReturnOK() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .basePath("/api/room/")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().response();

        List<?> rooms = response.jsonPath().getList("rooms");    // adjust key if different
        assertThat(rooms, is(not(empty())));

        // Validate fields of first room
        assertThat(response.jsonPath().getString("rooms[0].roomid"), is(not(blankOrNullString())));
        assertThat(response.jsonPath().getString("rooms[0].type"), is(not(blankOrNullString())));
    }

    @Test
    public void getRoomById_ShouldReturnOk() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .basePath("/api/room/1")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().response();

        // Validate fields of room
        assertThat(response.jsonPath().getString("roomid"), is("1"));
        assertThat(response.jsonPath().getString("type"), is("Single"));
    }

    @Test
    public void getRoomById_ShouldHaveValidStructure() {

        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .basePath("api/room/2/")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().response();

        // Validate top-level fields exist
        assertThat(response.jsonPath().getInt("roomid"), is(notNullValue()));
        assertThat(response.jsonPath().getString("roomName"), is(notNullValue()));
        assertThat(response.jsonPath().getString("type"), is(notNullValue()));
        assertThat(response.jsonPath().getBoolean("accessible"), is(notNullValue()));
        assertThat(response.jsonPath().getString("image"), is(notNullValue()));
        assertThat(response.jsonPath().getString("description"), is(notNullValue()));
        assertThat(response.jsonPath().getInt("roomPrice"), is(notNullValue()));

        // Validate "features" is a non-empty list
        List<String> features = response.jsonPath().getList("features");
        assertThat(features, is(not(empty())));
        features.forEach(f -> assertThat(f, is(notNullValue())));
    }

    @Test
    public void postMessage_ShouldReturnSuccess() {
        Map<String, Object> requestBody = Map.of(
                "name", "John",
                "email", "john.doe@gmail.com",
                "phone", "87778887799",
                "subject", "Inquiry booking",
                "description", "I'd like to book a double room for the weekend!");

        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .basePath("/api/message")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .extract().response();
    }

    //Negative Test
    @Test
    public void getRoomByInvalidId_ShouldReturnNotFound() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .basePath("/api/room/10")
                .when()
                .get()
                .then()
                .statusCode(404)
                .extract().response();
    }

    @Test
    public void postMessageInvalidBody_ShouldReturnBadRequest() {
        Map<String, Object> requestBody = Map.of(
                "name", "John",
                "email", "john.doe@gmail.com",
                "subject", "Inquiry booking",
                "description", "I'd like to book a double room for the weekend!");

        Response response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .basePath("/api/message")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(400)
                .extract().response();

        //verify api returns the correct error messages
        List<String> errorMessages = response.jsonPath().getList("");
        System.out.println("Validation errors: " + errorMessages);
        assertThat(errorMessages, hasItems("Phone may not be blank", "Phone must be set"));
    }
}
