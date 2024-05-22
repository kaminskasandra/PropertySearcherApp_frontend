package com.example.propertysearcherproject.domain;

import lombok.Data;

@Data
public class User {
    private long userId;
    private String userName;
    private String userLastName;
    private String mail;
    private String password;
}
