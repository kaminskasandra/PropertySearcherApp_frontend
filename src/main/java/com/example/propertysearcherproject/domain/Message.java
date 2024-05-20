package com.example.propertysearcherproject.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Message {
    private String email;
    private String text;
    private LocalDate date;
    private MessageCategory messageCategory;

    public Message(String email, String text, LocalDate date, MessageCategory messageCategory) {
        this.email = email;
        this.text = text;
        this.date = date;
        this.messageCategory = messageCategory;
    }
}
