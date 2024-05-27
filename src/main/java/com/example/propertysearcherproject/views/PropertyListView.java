package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.domain.Property;
import com.example.propertysearcherproject.domain.PropertyType;
import com.example.propertysearcherproject.domain.User;
import com.example.propertysearcherproject.integration.PropertyBackendIntegrationClient;
import com.example.propertysearcherproject.integration.WeatherIntegration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Route("property-list")
@org.springframework.stereotype.Component
@Scope("prototype")
public class PropertyListView extends VerticalLayout {

    private final Grid<Property> grid;
    private List<Property> properties = new ArrayList<>();

    public PropertyListView(PropertyBackendIntegrationClient propertyBackendIntegrationClient, WeatherIntegration weatherIntegration) {
        try {
            User username = (User) VaadinSession.getCurrent().getAttribute("username");
            Span welcomeLabel = new Span("Welcome to the Property Searcher, " + username.getUserName() + "!");
            add(welcomeLabel);
        } catch (Exception ignored) {

        }
        List<LinkedHashMap<String, Object>> allProperty = propertyBackendIntegrationClient.getAllProperty();

        for (LinkedHashMap<String, Object> property : allProperty) {
            properties.add(Property.builder()
                    .propertyId(Long.valueOf((Integer) property.get("propertyId")))
                    .propertyType(PropertyType.valueOf((String) property.get("propertyType")))
                    .price((Double) property.get("price"))
                    .address((String) property.get("address"))
                    .area((Double) property.get("area"))
                    .description((String) property.get("description")).build());
        }

        Span title = new Span("PROPERTY SEARCHER");
        title.getStyle().set("font-size", "24px");

        HorizontalLayout menuLayout = getMenuLayout(weatherIntegration);

        TextField searchField = new TextField();
        searchField.setPlaceholder("Search by address");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterGrid(event.getValue()));


        grid = new Grid<>(Property.class);
        grid.setItems(properties);
        grid.removeAllColumns();
        grid.addColumn(Property::getPropertyId).setHeader("ID");
        grid.addColumn(Property::getPropertyType).setHeader("Type");
        grid.addColumn(Property::getPrice).setHeader("Price");
        grid.addColumn(Property::getAddress).setHeader("Address");
        grid.addColumn(Property::getArea).setHeader("Area");

        grid.addItemClickListener(event -> {
            Property property = event.getItem();
            getUI().ifPresent(ui -> ui.navigate(PropertyDetailsView.class, property.getPropertyId()));
        });

        setAlignItems(Alignment.CENTER);
        add(title, menuLayout, searchField, grid);
    }

    private HorizontalLayout getMenuLayout(WeatherIntegration weatherIntegration) {
        Button appointmentsButton = new Button("Appointments");
        Button yourAccountButton = new Button("Your Account");
        Button messagesButton = new Button("Messages");
        Button offersButton = new Button("Your offers");

        appointmentsButton.addClickListener(event -> navigateTo(AppointmentsView.class));
        yourAccountButton.addClickListener(event -> navigateTo(YourAccountView.class));
        messagesButton.addClickListener(event -> navigateTo(MessagesView.class));
        offersButton.addClickListener(event -> navigateTo(YourOffersView.class));

        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setAlignItems(Alignment.CENTER);
        menuLayout.add(appointmentsButton, yourAccountButton, messagesButton, offersButton);


        Span weatherInfo = new Span();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LinkedHashMap<LinkedHashMap<String, Object>, LinkedHashMap<String, Object>> weather =
                    (LinkedHashMap<LinkedHashMap<String, Object>, LinkedHashMap<String, Object>>) objectMapper.readValue(weatherIntegration.getActualWeather("Warsaw"), Object.class);

            LinkedHashMap<String, Object> currentWeather = weather.get("current");

            weatherInfo.setText("Actual Weather: " + currentWeather.get("temp_c") + "°C @Powered by WeatherApi.com");
        } catch (Exception e) {
            weatherInfo.setText("Weather info not available");
        }
        weatherInfo.getStyle().set("margin-left", "auto"); // Umieszczamy po prawej stronie

        menuLayout.add(weatherInfo);

        return menuLayout;
    }

    private void filterGrid(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            grid.setItems(properties);
        } else {
            List<Property> filteredProperties = properties.stream()
                    .filter(property -> property.getAddress().toLowerCase().contains(filterText.toLowerCase()))
                    .collect(Collectors.toList());
            grid.setItems(filteredProperties);
        }
    }

    private void navigateTo(Class<? extends Component> viewClass) {
        getUI().ifPresent(ui -> ui.navigate(viewClass));
    }
}
