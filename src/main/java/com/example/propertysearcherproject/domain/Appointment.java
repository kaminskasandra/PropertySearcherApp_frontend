package com.example.propertysearcherproject.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class Appointment {
    private long appointmentId;
    private LocalDate dateOfMeeting;
    private String description;
    private Long propertyId;
    private Long userId;

    public Appointment(long appointmentId, LocalDate dateOfMeeting, String description, Long propertyId, Long userId) {
        this.appointmentId = appointmentId;
        this.dateOfMeeting = dateOfMeeting;
        this.description = description;
        this.propertyId = propertyId;
        this.userId = userId;
    }
}
