package com.examples.models;

public class Message {
    public String name;
    public String email;
    public String phone;
    public String subject;
    public String description;

    public Message(String name, String email, String phone, String subject, String description) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.subject = subject;
        this.description = description;
    }
}
