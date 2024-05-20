package com.example.propertysearcherproject.domain;

import lombok.Data;

@Data
public class User {

    private long userId;
    private String name;
    private String lastName;
    private String mail;

    public User(long userId, String name, String lastName, String mail) {
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.mail = mail;
    }
}
