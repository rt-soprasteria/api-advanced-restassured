package com.examples.tests;

import com.examples.models.Message;
import com.examples.utils.TestDataFactory;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class ShadyMeadowsTestDataFactory {
    private Message message;

    @BeforeAll
    public static void setup() {
        // Base setup for all tests
        RestAssured.baseURI = "https://automationintesting.online";
    }

    @BeforeEach
    public void createTestData() {
        // Create test data before each test
        message = TestDataFactory.createRandomMessage();
        System.out.println("Test data: " + message.name + ", " + message.email + ", " + message.phone + ", " + message.subject + ", " + message.description);
    }

    @Test
    public void sendMessageToShadyMeadows() {
        RestAssured
                .given()
                .basePath("/api/message")
                .header("Content-Type", "application/json")
                .body(message)
                .when()
                .post()
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .extract().response();
    }

    @AfterEach
    public void tearDown() {
        // Shady Meadows doesn’t provide a DELETE /message endpoint,
        // but if it did, you would clean up here.
    }
}

