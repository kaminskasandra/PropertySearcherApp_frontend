package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.domain.Property;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.util.HashMap;
import java.util.Map;


@Route("property-details")
public class PropertyDetailsView extends VerticalLayout implements HasUrlParameter<Long> {

    private PropertyService propertyService;

    public PropertyDetailsView(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long propertyId) {
        removeAll();
        if (propertyId != null) {
            Property property = propertyService.getPropertyById(propertyId);
            if (property != null) {
                addHeader(property);
                addDetails(property);
            } else {
                add(new Text("Property not found"));
            }
        } else {
            add(new Text("No property ID provided"));
        }
    }

    private void addHeader(Property property) {
        Span header = new Span("Property Details");
        header.getStyle().setFontSize("24px").setFontWeight("bold");
        add(header);

        Button backButton = new Button("Back to Property List");
        backButton.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate(PropertyListView.class)));
        add(backButton);
    }

    private void addDetails(Property property) {
        Map<String, String> details = new HashMap<>();
        details.put("ID", property.getId().toString());
        details.put("Type", property.getPropertyType().toString());
        details.put("Price", Double.toString(property.getPrice()));
        details.put("Address", property.getAddress());
        details.put("Area", Double.toString(property.getArea()));
        details.put("Description", property.getDescription().toString());

        VerticalLayout detailsLayout = new VerticalLayout();
        detailsLayout.setDefaultHorizontalComponentAlignment(Alignment.START);
        details.forEach((key, value) -> {
            Span detailLabel = new Span(key + ": ");
            Text detailValue = new Text(value);
            detailLabel.getStyle().set("font-weight", "bold");
            detailsLayout.add(detailLabel, detailValue);
        });

        add(detailsLayout);
        setHorizontalComponentAlignment(Alignment.CENTER, detailsLayout);
    }
}