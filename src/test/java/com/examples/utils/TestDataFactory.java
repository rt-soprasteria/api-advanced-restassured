package com.examples.utils;

import com.examples.models.Message;
import com.github.javafaker.Faker;

public class TestDataFactory {
    private static final Faker faker = new Faker();

    public static Message createRandomMessage() {
        return new Message(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.phoneNumber().phoneNumber(),
                "Inquiry about Shady Meadows",
                faker.lorem().sentence(8)
        );
    }
}