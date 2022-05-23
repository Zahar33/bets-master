package com.example.model;


public class Bookmaker extends Person {

    public static final String EMAIL = "qwe@mail.ru";

    public Bookmaker() {
    }

    public Bookmaker(String firstName, String lastName, String password, String email) {
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        setEmail(email);
    }

}
