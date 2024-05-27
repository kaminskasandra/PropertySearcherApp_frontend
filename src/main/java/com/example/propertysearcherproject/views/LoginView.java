package com.example.propertysearcherproject.views;
import com.example.propertysearcherproject.integration.CurrencyExchangeIntegration;
import com.example.propertysearcherproject.integration.UserBackendIntegrationClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("")
public class LoginView extends Div {
    private final LoginForm loginForm;
    private RegisterForm registerForm;
    private final Button switchToRegisterButton;
    private final Button switchToLoginButton;
    private final UserBackendIntegrationClient userBackendIntegrationClient;


    public LoginView(UserBackendIntegrationClient userBackendIntegrationClient) {
        this.userBackendIntegrationClient = userBackendIntegrationClient;
        loginForm = new LoginForm(this.userBackendIntegrationClient);
        registerForm = new RegisterForm(this.userBackendIntegrationClient);
        switchToRegisterButton = new Button("Don't have an account? Register here.");
        switchToLoginButton = new Button("Already have an account? Login here.");
        add(loginForm, switchToRegisterButton);

        switchToRegisterButton.addClickListener(event -> switchToRegisterForm());
        switchToLoginButton.addClickListener(event -> switchToLoginForm());
    }

    private void switchToRegisterForm() {
        removeAll();
        add(registerForm, switchToLoginButton);
    }

    private void switchToLoginForm() {
        removeAll();
        add(loginForm, switchToRegisterButton);
    }
}
