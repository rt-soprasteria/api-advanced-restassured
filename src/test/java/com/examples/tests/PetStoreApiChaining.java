package com.examples.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PetStoreApiChaining {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    @Test
    public void testChainedPetstoreRequests() throws InterruptedException {
        RestAssured.useRelaxedHTTPSValidation();

        // Step 1 — POST create a pet
        String postBody = "{"
                + "\"category\": {\"id\": 0, \"name\": \"Anfisa\"},"
                + "\"name\": \"Mufasa\","
                + "\"photoUrls\": [\"string\"],"
                + "\"tags\": [{\"id\": 0, \"name\": \"string\"}],"
                + "\"status\": \"available\""
                + "}";

        long petId = given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .basePath("/pet")
                .header("Content-Type", "application/json")
                .body(postBody)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().path("id");

        //Store id from the response into variable petId
        //long petId = postResponse.jsonPath().getLong("id");
        System.out.println("Created pet ID: " + petId);

        // Step 2 — PUT update the pet using ID from POST
        String putBody = "{"
                + "\"id\": " + petId + ","
                + "\"category\": {\"id\": 0, \"name\": \"Cutepet\"},"
                + "\"name\": \"Simba\","
                + "\"photoUrls\": [\"string\"],"
                + "\"tags\": [{\"id\": 0, \"name\": \"string\"}],"
                + "\"status\": \"available\""
                + "}";

        Response putResponse = given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .basePath("/pet")
                .header("Content-Type", "application/json")
                .body(putBody)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Updated pet: " + putResponse.asString());
        Thread.sleep(2000); //Petstore uses a queue which you can't control, so sometimes it takes a while for the update to come through

        // Step 3 — GET the updated pet and verify fields
        Response getResponse = given()
                .baseUri(BASE_URL)
                .basePath("/pet/" + petId)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Fetched pet: " + getResponse.asString());

        assertThat(getResponse.jsonPath().getString("name"), equalTo("Simba"));
        assertThat(getResponse.jsonPath().getString("category.name"), equalTo("Cutepet"));
        assertThat(getResponse.jsonPath().getString("status"), equalTo("available"));
    }
}
