package com.exercises.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class Exercise3_Hooks {
    static String createdObjectId;
    static String baseURI = "https://api.restful-api.dev";

    @BeforeAll
    public static void setup() {
        // Create a test object
        Map<String, Object> obj = Map.of(
                "name", "Test Object",
                "data", Map.of("color", "Blue", "price", 99.99)
        );
        Response response = RestAssured
                .given()
                .baseUri(baseURI)
                .basePath("/objects")
                .header("Content-Type", "application/json")
                .body(obj)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().response();

        // Store the created object's ID for test & teardown
        createdObjectId = response.jsonPath().getString("id");
        System.out.println("Created: " + response.getBody().asString());
    }

    @Test
    public void testUpdateObject() {
        // Update the object
        Map<String, Object> updatedData = Map.of(
                "name", "Test Object",
                "data", Map.of("color", "Red", "price", 79.99));

        Response response = RestAssured
                .given()
                .baseUri(baseURI)
                .basePath("/objects/" + createdObjectId)
                .header("Content-Type", "application/json")
                .body(updatedData)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Updated: " + response.getBody().asString());

        // Assert the update
        Map<String, Object> obj = RestAssured
                .given()
                .baseUri(baseURI)
                .basePath("objects/" + createdObjectId)
                .when()
                .get()
                .jsonPath().getMap("$");

        assertEquals(((Map)obj.get("data")).get("color"), "Red");
        System.out.println("Read: " + obj.values().toString());
    }

    @AfterAll
    public static void teardown() {
        // Delete the object
        RestAssured.delete("https://api.restful-api.dev/objects/" + createdObjectId);
        System.out.println("Deleted item with id: " + createdObjectId);
    }
}
