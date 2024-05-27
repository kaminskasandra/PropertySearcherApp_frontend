package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.domain.Appointment;
import com.example.propertysearcherproject.domain.User;
import com.example.propertysearcherproject.integration.AppointmentBackendIntegrationClient;
import com.example.propertysearcherproject.integration.PropertyBackendIntegrationClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Route("appointments")
@org.springframework.stereotype.Component
@Scope("prototype")
public class  AppointmentsView extends VerticalLayout {

    private final AppointmentBackendIntegrationClient appointmentBackendIntegrationClient;
    private List<Appointment> allAppointments = new ArrayList<>();
    private final Grid<Appointment> appointmentsGrid = new Grid<>(Appointment.class);
    private final DatePicker datePicker = new DatePicker();
    private final PropertyBackendIntegrationClient propertyBackendIntegrationClient;

    @Autowired
    public AppointmentsView(AppointmentBackendIntegrationClient appointmentBackendIntegrationClient, PropertyBackendIntegrationClient propertyBackendIntegrationClient) {
        this.appointmentBackendIntegrationClient = appointmentBackendIntegrationClient;
        this.propertyBackendIntegrationClient = propertyBackendIntegrationClient;

        Button mainViewButton = new Button("BACK TO MAIN PAGE");
        mainViewButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(PropertyListView.class)));

        H1 header = new H1("APPOINTMENTS");
        header.getStyle().set("font-size", "22px");

        User user = (User)VaadinSession.getCurrent().getAttribute("username");
        if (user != null) {
            allAppointments = appointmentBackendIntegrationClient.getAppointmentsByUserId(user.getUserId());

            datePicker.setLabel("Select Date");
            datePicker.addValueChangeListener(event -> updateGrid(event.getValue()));

            appointmentsGrid.removeAllColumns();
            appointmentsGrid.addColumn(Appointment::getDescription).setHeader("MEETING DESCRIPTION");
            appointmentsGrid.addColumn(Appointment::getDateOfMeeting).setHeader("DATE OF MEETING");
            appointmentsGrid.addColumn(Appointment::getPropertyId).setHeader("PROPERTY ID");
            appointmentsGrid.addColumn(Appointment::getUserId).setHeader("USER ID");

            appointmentsGrid.setItems(allAppointments);

            Button cancelButton = new Button("Cancel Appointment");
            cancelButton.setEnabled(false);

            appointmentsGrid.asSingleSelect().addValueChangeListener(event ->
                    cancelButton.setEnabled(event.getValue() != null));

            cancelButton.addClickListener(event -> {
                Appointment selectedAppointment = appointmentsGrid.asSingleSelect().getValue();
                if (selectedAppointment != null) {
                    appointmentBackendIntegrationClient.deleteAppointmentById(selectedAppointment.getAppointmentId());
                    allAppointments.remove(selectedAppointment);
                    appointmentsGrid.setItems(allAppointments);
                    getElement().executeJs("alert('Appointment canceled: " + selectedAppointment.getDescription() + "')");
                }
            });

            ComboBox<Long> propertyIdField = new ComboBox<>("Property ID");
            propertyIdField.setItems(getAllPropertyIds());

            TextField descriptionField = new TextField("Description");
            DatePicker meetingDateField = new DatePicker("Date of Meeting");

            Button saveButton = new Button("Save Appointment");
            saveButton.addClickListener(event -> {
                Long propertyId = propertyIdField.getValue();
                String description = descriptionField.getValue();
                LocalDate meetingDate = meetingDateField.getValue();

                if (propertyId != null && description != null && meetingDate != null) {
                    Appointment newAppointment = Appointment.builder()
                            .propertyId(propertyId)
                            .description(description)
                            .dateOfMeeting(meetingDate)
                            .userId(user.getUserId())
                            .build();

                    appointmentBackendIntegrationClient.saveAppointment(newAppointment);
                    allAppointments.add(newAppointment);
                    appointmentsGrid.setItems(allAppointments);
                    Notification.show("Appointment saved", 3000, Notification.Position.MIDDLE);

                    propertyIdField.clear();
                    descriptionField.clear();
                    meetingDateField.clear();
                } else {
                    Notification.show("All fields must be filled out", 3000, Notification.Position.MIDDLE);
                }
            });

            HorizontalLayout formLayout = new HorizontalLayout(propertyIdField, descriptionField, meetingDateField, saveButton);
            add(mainViewButton, header, datePicker, appointmentsGrid, cancelButton, formLayout);
        } else {
            Notification.show("No user logged in", 3000, Notification.Position.MIDDLE);
        }
    }

    private void updateGrid(LocalDate date) {
        List<Appointment> filteredAppointments = allAppointments.stream()
                .filter(appointment -> appointment.getDateOfMeeting().equals(date))
                .collect(Collectors.toList());
        appointmentsGrid.setItems(filteredAppointments);
    }

    private List<Long> getAllPropertyIds() {
        List<LinkedHashMap<String, Object>> allProperties = propertyBackendIntegrationClient.getAllProperty();
        return allProperties.stream()
                .map(property -> Long.valueOf((Integer) property.get("propertyId")))
                .collect(Collectors.toList());
    }
}
