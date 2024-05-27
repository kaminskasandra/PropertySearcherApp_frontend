package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.domain.Property;
import com.example.propertysearcherproject.domain.PropertyType;
import com.example.propertysearcherproject.domain.User;
import com.example.propertysearcherproject.integration.PropertyBackendIntegrationClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

@Route("user-properties")
public class YourOffersView extends VerticalLayout {

    private final PropertyBackendIntegrationClient propertyBackendIntegrationClient;

    public YourOffersView(PropertyBackendIntegrationClient propertyBackendIntegrationClient) {
        this.propertyBackendIntegrationClient = propertyBackendIntegrationClient;

        Button mainViewButton = new Button("BACK TO MAIN PAGE");
        mainViewButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(PropertyListView.class)));

        H1 header = new H1("YOUR OFFERS");
        header.getStyle().set("font-size", "22px");

        Grid<Property> propertyGrid = new Grid<>(Property.class);
        propertyGrid.setColumns("propertyId", "propertyType", "price", "address", "area", "description");

        Button addButton = new Button("Add Property");
        User user = loadUserProperties(propertyGrid);
        addButton.addClickListener(event -> showAddPropertyForm(user));

        add(mainViewButton, header, propertyGrid, addButton);

    }

    private User loadUserProperties(Grid<Property> propertyGrid) {
        try {
            User user = (User) VaadinSession.getCurrent().getAttribute("username");
            List<Property> properties = propertyBackendIntegrationClient.getPropertiesByUser(user.getUserId());
            propertyGrid.setItems(properties);
            return user;
        } catch (Exception e) {
            Notification.show("Error loading properties: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }

        return null;
    }

    private void showAddPropertyForm(User user) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        FormLayout formLayout = new FormLayout();

        ComboBox<PropertyType> propertyTypeComboBox = new ComboBox<>("Property Type");
        propertyTypeComboBox.setItems(PropertyType.values());

        NumberField priceField = new NumberField("Price");
        TextField addressField = new TextField("Address");
        NumberField areaField = new NumberField("Area");
        TextArea descriptionField = new TextArea("Description");
        descriptionField.setWidthFull();
        descriptionField.setHeight("150px");

        formLayout.add(propertyTypeComboBox, priceField, addressField, areaField, descriptionField);

        Button saveButton = new Button("Save", event -> {
            PropertyType propertyType = propertyTypeComboBox.getValue();
            Double price = priceField.getValue();
            String address = addressField.getValue();
            Double area = areaField.getValue();
            String description = descriptionField.getValue();

            propertyBackendIntegrationClient.saveProperty(Property.builder()
                    .propertyType(propertyType)
                    .address(address)
                    .price(price)
                    .area(area)
                    .description(description)
                    .userId(user.getUserId())
                    .build());

            Notification.show("Property added:\n" +
                            "Type: " + propertyType + "\n" +
                            "Price: " + price + "\n" +
                            "Address: " + address + "\n" +
                            "Area: " + area + "\n" +
                            "Description: " + description,
                    5000, Notification.Position.MIDDLE);

            dialog.close();
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttonLayout);
        dialog.add(dialogLayout);

        dialog.open();
    }
}

