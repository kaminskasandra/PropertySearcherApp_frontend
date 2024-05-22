package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.domain.User;
import com.example.propertysearcherproject.integration.UserBackendIntegrationClient;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Route("your-account")
@Component
@Scope("prototype")
public class YourAccountView extends VerticalLayout {

    private final Grid<User> userGrid = new Grid<>(User.class);
    private final Binder<User> binder = new Binder<>(User.class);
    private User currentUser;
    private final UserBackendIntegrationClient userBackendIntegrationClient;

    @Autowired
    public YourAccountView(UserBackendIntegrationClient userBackendIntegrationClient) {
        this.userBackendIntegrationClient = userBackendIntegrationClient;
        User user = null;
                
        try {
            user = (User) VaadinSession.getCurrent().getAttribute("username");
        } catch (Exception ignored) {

        }

        Button mainViewButton = new Button("BACK TO MAIN PAGE");
        mainViewButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(PropertyListView.class)));

        H1 header = new H1("YOUR ACCOUNT");
        header.getStyle().set("font-size", "22px");

        currentUser = userBackendIntegrationClient.getUserById(user.getUserId());
        userGrid.setItems(currentUser);
        userGrid.setColumns("userId", "userName", "userLastName", "mail");

        Button editButton = new Button("Edit personal data");
        editButton.addClickListener(event -> openEditDialog(currentUser));

        Button deleteButton = new Button("Delete Account");
        deleteButton.addClickListener(event -> openDeleteConfirmationDialog(currentUser));

        HorizontalLayout buttonLayout = new HorizontalLayout(editButton, deleteButton);
        add(mainViewButton, header, userGrid, buttonLayout);
    }

    private void openEditDialog(User currentUser) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        dialog.setHeight("300px");

        TextField nameField = new TextField("Name");
        TextField lastNameField = new TextField("Last Name");

        binder.bind(nameField, User::getUserName, User::setUserName);
        binder.bind(lastNameField, User::getUserLastName, User::setUserLastName);

        binder.readBean(currentUser);

        FormLayout formLayout = new FormLayout();
        formLayout.add(nameField, lastNameField);

        Button saveButton = new Button("Save", event -> {
            try {
                binder.writeBean(currentUser);
                userBackendIntegrationClient.updateUser(currentUser, currentUser.getUserId());
                userGrid.getDataProvider().refreshItem(currentUser);
                dialog.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        HorizontalLayout dialogButtonLayout = new HorizontalLayout(saveButton, cancelButton);
        VerticalLayout dialogLayout = new VerticalLayout(formLayout, dialogButtonLayout);
        dialog.add(dialogLayout);

        dialog.open();
    }

    private void openDeleteConfirmationDialog(User currentUser) {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.setWidth("300px");
        confirmationDialog.setHeight("150px");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);

        dialogLayout.add(new Text("Are you sure you want to delete your account?"));

        Button confirmButton = new Button("Yes", event -> {
            userBackendIntegrationClient.deleteUserById(currentUser.getUserId());
            userGrid.setItems(userBackendIntegrationClient.getUserById(currentUser.getUserId()));
            confirmationDialog.close();
            getElement().executeJs("alert('Account deleted')");
        });

        Button cancelButton = new Button("No", event -> confirmationDialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);
        buttonLayout.setSpacing(true);

        dialogLayout.add(buttonLayout);
        confirmationDialog.add(dialogLayout);

        confirmationDialog.open();
    }
}