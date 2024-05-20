package com.example.propertysearcherproject.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RealEstateAgency {
    private long agencyId;
    private String agencyName;
    private String phoneNumber;
}
