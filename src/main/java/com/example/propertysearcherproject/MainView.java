package com.example.propertysearcherproject;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route
public class MainView extends VerticalLayout {
    private final Grid<Property> grid = new Grid<>(Property.class);
    private final PropertyService propertyService = PropertyService.getInstance();
    private final TextField filter = new TextField();

    public MainView() {

        MenuBar menuBar = new MenuBar();

        MenuItem appName = menuBar.addItem("PROPERTY SEARCHER");
        appName.addClassNames(LumoUtility.Background.PRIMARY,
                LumoUtility.TextColor.PRIMARY_CONTRAST);

        MenuItem account = menuBar.addItem("YOUR ACCOUNT");
        SubMenu shareSubMenu = account.getSubMenu();

        shareSubMenu.addItem("Settings").addClassNames(
                LumoUtility.Background.PRIMARY,
                LumoUtility.TextColor.PRIMARY_CONTRAST);
        shareSubMenu.addItem("Get Link");

        MenuItem appointment = menuBar.addItem("APPOINTMENTS");

        MenuItem messages = menuBar.addItem("MESSAGES");
        SubMenu shareMessagesMenu = messages.getSubMenu();

        shareMessagesMenu.addItem("Sent").addClassNames(
                LumoUtility.Background.PRIMARY,
                LumoUtility.TextColor.PRIMARY_CONTRAST);
        shareMessagesMenu.addItem("Received");
        add(menuBar);

        filter.setPlaceholder("Filter by property address");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> update());

        grid.setColumns("propertyType", "price", "address", "area");
        add(filter, grid);
        setSizeFull();
        refresh();
    }
    public void refresh() {
        grid.setItems(propertyService.getProperties());
    }
    private void update() {
        grid.setItems(propertyService.findByAddress(filter.getValue()));
    }
}