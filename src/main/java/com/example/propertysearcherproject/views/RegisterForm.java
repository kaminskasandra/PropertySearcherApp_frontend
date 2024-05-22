package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.domain.User;
import com.example.propertysearcherproject.integration.UserBackendIntegrationClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Route("register")
public class RegisterForm extends FormLayout {
    @Autowired
    public RegisterForm(UserBackendIntegrationClient userBackendIntegrationClient) {
        TextField username = new TextField("Username");
        EmailField email = new EmailField("Email");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm Password");
        Button registerButton = new Button("Register");

        registerButton.addClickListener(event -> {
            if (password.getValue().equals(confirmPassword.getValue())) {
                User newUser = new User();
                newUser.setUserName(username.getValue());
                newUser.setMail(email.getValue());
                newUser.setPassword(password.getValue());

                try {
                    User user = userBackendIntegrationClient.saveUser(newUser);
                    Notification.show("Registered with username: " + user.getUserName());
                    VaadinSession.getCurrent().setAttribute("username", user);
                    UI.getCurrent().navigate("property-list");
                } catch (Exception e) {
                    Notification.show("Error: " + e.getMessage(), 3000, Position.MIDDLE);
                }
            } else {
                Notification.show("Passwords do not match", 3000, Position.MIDDLE);
            }
        });

        add(username, email, password, confirmPassword, registerButton);
    }
}