package com.example.propertysearcherproject.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class Message {

    private Long messageId;
    private String text;
    private LocalDate date;
    private MessageCategory messageCategory;
    private String fromUserMail;
    private String toUserMail;
}
