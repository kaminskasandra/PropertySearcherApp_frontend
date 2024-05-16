package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.Property;
import com.example.propertysearcherproject.PropertyService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route("property-list")
public class PropertyListView extends VerticalLayout {

    public PropertyListView(PropertyService propertyService) {
        List<Property> properties = propertyService.getProperties();

        Span title = new Span("PROPERTY SEARCHER");
        title.getStyle().set("font-size", "24px");

        Button appointmentsButton = new Button("Appointments");
        Button yourAccountButton = new Button("Your Account");
        Button messagesButton = new Button("Messages");

        appointmentsButton.addClickListener(event -> navigateTo(AppointmentsView.class));
        yourAccountButton.addClickListener(event -> navigateTo(YourAccountView.class));
        messagesButton.addClickListener(event -> navigateTo(MessagesView.class));

        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setAlignItems(Alignment.CENTER);
        menuLayout.add(appointmentsButton, yourAccountButton, messagesButton);

        Grid<Property> grid = new Grid<>();
        grid.setItems(properties);
        grid.addColumn(Property::getId).setHeader("ID");
        grid.addColumn(Property::getPropertyType).setHeader("Type");
        grid.addColumn(Property::getPrice).setHeader("Price");
        grid.addColumn(Property::getAddress).setHeader("Address");
        grid.addColumn(Property::getArea).setHeader("Area");

        grid.addItemClickListener(event -> {
            Property property = event.getItem();
            getUI().ifPresent(ui -> ui.navigate(PropertyDetailsView.class, property.getId()));
        });

        setAlignItems(Alignment.CENTER);
        add(title, menuLayout, grid);
    }

    private void navigateTo(Class<? extends Component> viewClass) {
        getUI().ifPresent(ui -> ui.navigate(viewClass));
    }
}