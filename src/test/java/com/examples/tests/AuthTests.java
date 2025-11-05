package com.examples.tests;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AuthTests {

    @Test
    public void testBasicAuth() {
        RestAssured.useRelaxedHTTPSValidation();
        System.out.println("Running Basic Auth test...");

        Response response = RestAssured
                .given()
                .auth().preemptive().basic("user", "passwd")
                .when()
                .get("https://httpbin.org/basic-auth/user/passwd")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Response:\n" + response.asPrettyString());

        assertThat(response.jsonPath().getBoolean("authenticated"), is(true));
        assertThat(response.jsonPath().getString("user"), equalTo("user"));

        System.out.println("Basic Auth test passed!\n");
    }

    @Test
    public void testBearerToken() {
        RestAssured.useRelaxedHTTPSValidation();
        System.out.println("Running Bearer Token test...");

        String token = "12345abcde";

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("https://httpbin.org/bearer")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Response:\n" + response.asPrettyString());

        assertThat(response.jsonPath().getBoolean("authenticated"), is(true));
        assertThat(response.jsonPath().getString("token"), equalTo(token));

        System.out.println("Bearer Token test passed!\n");
    }

    @Test
    public void testCustomHeader() {
        RestAssured.useRelaxedHTTPSValidation();
        System.out.println("Running Custom Header test...");

        String apiKey = "my-secret-key";

        Response response = RestAssured
                .given()
                .header("X-API-Key", apiKey)
                .when()
                .get("https://httpbin.org/headers")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Response:\n" + response.asPrettyString());

        String returnedKey = response.jsonPath().getString("headers.X-Api-Key");
        assertThat(returnedKey, equalTo(apiKey));

        System.out.println("Custom Header test passed!\n");
    }
}
