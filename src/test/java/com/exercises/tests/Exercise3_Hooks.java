package com.exercises.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class Exercise3_Hooks {
    static String createdObjectId;
    static String baseURI = "";

    @BeforeAll
    public static void setup() {
        // Create a test object
        
    }

    @Test
    public void testUpdateObject() {
        // Update the object
        
    }

    @AfterAll
    public static void teardown() {
        // Delete the object
        
    }
}
