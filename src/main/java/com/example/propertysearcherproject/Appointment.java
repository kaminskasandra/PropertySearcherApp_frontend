package com.example.propertysearcherproject;

import lombok.Data;

import java.time.LocalDate;
@Data
public class Appointment {
    private long appointmentId;
    private LocalDate dateOfMeeting;
    private String description;

    public Appointment(LocalDate dateOfMeeting, String description) {
        this.dateOfMeeting = dateOfMeeting;
        this.description = description;
    }
}
