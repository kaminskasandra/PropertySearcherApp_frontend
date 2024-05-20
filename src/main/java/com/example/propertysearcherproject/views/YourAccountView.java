package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.domain.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route("your-account")
public class YourAccountView extends VerticalLayout {
    private final List<User> users = new ArrayList<>();

    public YourAccountView() {

        Button mainViewButton = new Button("BACK TO MAIN PAGE");
        mainViewButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(PropertyListView.class)));

        H1 header = new H1("YOUR ACCOUNT");
        header.getStyle().set("font-size", "22px");

        users.add(new User(1L, "John", "Doe", "john@example.com"));

        Grid<User> userGrid = new Grid<>(User.class);
        userGrid.setItems(users);

        userGrid.setColumns("userId", "name", "lastName", "mail");
        add(mainViewButton, header, userGrid);
    }
}