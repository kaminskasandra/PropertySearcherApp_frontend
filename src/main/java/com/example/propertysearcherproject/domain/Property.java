package com.example.propertysearcherproject.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Property {
    private Long id;
    private PropertyType propertyType;
    private double price;
    private String address;
    private double area;
    private String description;
}
