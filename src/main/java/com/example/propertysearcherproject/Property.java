package com.example.propertysearcherproject;

import lombok.Data;

@Data
public class Property {
    private Long id;
    private PropertyType propertyType;
    private double price;
    private String address;
    private double area;
    private String description;

    public Property(Long id, PropertyType propertyType, double price, String address, double area, String description) {
        this.id = id;
        this.propertyType = propertyType;
        this.price = price;
        this.address = address;
        this.area = area;
        this.description = description;
    }
}
