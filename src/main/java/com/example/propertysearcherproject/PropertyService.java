package com.example.propertysearcherproject;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class PropertyService {
    private final List<Property> propertyList;
    private static PropertyService propertyService;

    private PropertyService() {
        this.propertyList = exampleData();
    }

    public static PropertyService getInstance() {
        if (propertyService == null) {
            propertyService = new PropertyService();
        }
        return propertyService;
    }

    public List<Property> getProperties() {
        return new ArrayList<>(propertyList);
    }

    public void addProperty(Property property) {
        this.propertyList.add(property);
    }

    private List<Property> exampleData() {
        List<Property> properties = new ArrayList<>();
        properties.add(new Property(1L, PropertyType.APARTMENT, 156000,"Wrocław, ul. Polna", 66.40, "example desc"));
        properties.add(new Property(2L, PropertyType.BUILDING_PLOT, 300000,  "Warszawa, ul. Mikołajska", 10.6, "example desc"));
        properties.add(new Property(3L, PropertyType.BUILDING_PLOT, 550000,  "Kraków, ul. Długa", 7.6, "example desc"));
        properties.add(new Property(4L, PropertyType.SINGLE_FAMILY_HOUSE, 1050000,  "Opole, ul. Słoneczna", 186.0, "example desc"));
        properties.add(new Property(5L, PropertyType.APARTMENT, 400000,  "Wrocław, ul. Majowa", 98.62, "example desc"));
        properties.add(new Property(6L, PropertyType.PREMISES_FOR_RENT,333000,  "Łódź, ul. Komedy", 100.5, "example desc"));
        properties.add(new Property(7L, PropertyType.BUILDING_PLOT, 159000,  "Wrocław, ul. Wiejska", 9.8, "example desc"));
        return properties;
    }

    public List<Property> findByAddress(String address) {
        return propertyList.stream().filter(property -> property.getAddress().contains(address))
                .collect(Collectors.toList());
    }

    public Property getPropertyById(Long propertyId) {
        for (Property property : exampleData()) {
            if (property.getId().equals(propertyId)) {
                return property;
            }
        }
        return null;
    }
}
