package com.exercises.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Map;

public class Exercise1_CRUDRequests {
    private static final String BASE_URL = "";

    @Test
    public void getObjectById_ShouldReturnOK() {
        
    }

    @Test
    public void createNewObject_ShouldReturnOK() {
        Map<String, Object> requestBody = Map.of(
                "name", "Test Object",
                "data", Map.of("year", "2024", "price", 99.99)
        );
    }
}
