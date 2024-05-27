package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.domain.Property;
import com.example.propertysearcherproject.integration.CurrencyExchangeIntegration;
import com.example.propertysearcherproject.integration.PropertyBackendIntegrationClient;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.LinkedHashMap;
import java.util.Map;


@Route("property-details")
@org.springframework.stereotype.Component
@Scope("prototype")
public class PropertyDetailsView extends VerticalLayout implements HasUrlParameter<Long> {

    private final PropertyBackendIntegrationClient propertyBackendIntegrationClient;
    private final CurrencyExchangeIntegration currencyExchangeIntegration;

    @Autowired
    public PropertyDetailsView(PropertyBackendIntegrationClient propertyBackendIntegrationClient, CurrencyExchangeIntegration currencyExchangeIntegration) {
        this.propertyBackendIntegrationClient = propertyBackendIntegrationClient;
        this.currencyExchangeIntegration = currencyExchangeIntegration;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long propertyId) {
        removeAll();
        if (propertyId != null) {
            Property property = propertyBackendIntegrationClient.getPropertyById(propertyId);
            if (property != null && property.getPropertyId() != null) {
                String currencyEurToPlnRatio = currencyExchangeIntegration.getCurrencyRatio();
                addHeader(property);
                addDetails(property, currencyEurToPlnRatio);
            } else {
                add(new Text("Property not found"));
            }
        }
    }

    private void addHeader(Property property) {
        Span header = new Span("Property Details");
        header.getStyle().set("font-size", "24px").set("font-weight", "bold");
        add(header);

        Button backButton = new Button("Back to Property List");
        backButton.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate(PropertyListView.class)));
        add(backButton);
    }

    private void addDetails(Property property, String currencyEurToPlnRatio) {
        Map<String, String> details = new LinkedHashMap<>();
        details.put("ID", property.getPropertyId().toString());
        details.put("Type", property.getPropertyType().toString());
        details.put("Price", Double.toString(property.getPrice()));
        details.put("Address", property.getAddress());
        details.put("Area", Double.toString(property.getArea()));
        details.put("Description", property.getDescription());
        details.put("Price in euro (Powered by @currency-exchange rapid-api)", String.valueOf((property.getPrice() * Double.parseDouble(currencyEurToPlnRatio))));

        VerticalLayout detailsLayout = new VerticalLayout();
        detailsLayout.setDefaultHorizontalComponentAlignment(Alignment.START);
        details.forEach((key, value) -> {
            Span detailLabel = new Span(key + ": " + value);
            detailLabel.getStyle().set("font-weight", "bold");
            detailsLayout.add(detailLabel);
        });

        add(detailsLayout);
        setHorizontalComponentAlignment(Alignment.CENTER, detailsLayout);
    }
}
