package com.example.propertysearcherproject;

public class Property {
    private PropertyType propertyType;
    private double price;
    private String address;
    private double area;

    public Property() {
    }

    public Property(PropertyType propertyType, double price, String address, double area) {
        this.propertyType = propertyType;
        this.price = price;
        this.address = address;
        this.area = area;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}
