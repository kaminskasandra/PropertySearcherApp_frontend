package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.domain.User;
import com.example.propertysearcherproject.integration.UserBackendIntegrationClient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;

public class LoginForm extends FormLayout {
    public LoginForm(UserBackendIntegrationClient userBackendIntegrationClient) {
        TextField userMail = new TextField("User email");
        PasswordField password = new PasswordField("Password");
        Button loginButton = new Button("Login");

        loginButton.addClickListener(event -> {
            String email = userMail.getValue();
            User user = userBackendIntegrationClient.getUserByMail(email);

            if (user != null) {
                Notification.show("Logged in with user email: " + user.getMail());
                VaadinSession.getCurrent().setAttribute("username", user);
                UI.getCurrent().navigate("property-list");
            } else {
                Notification.show("Invalid email or password", 3000, Notification.Position.MIDDLE);
            }
        });

        add(userMail, password, loginButton);
    }
}