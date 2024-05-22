package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.domain.Appointment;
import com.example.propertysearcherproject.integration.AppointmentBackendIntegrationClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Route("appointments")
@org.springframework.stereotype.Component
public class AppointmentsView extends VerticalLayout {

    private final AppointmentBackendIntegrationClient appointmentBackendIntegrationClient;
    private final List<Appointment> allAppointments = new ArrayList<>();
    private final Grid<Appointment> appointmentsGrid = new Grid<>(Appointment.class);
    private final DatePicker datePicker = new DatePicker();

    @Autowired
    public AppointmentsView(AppointmentBackendIntegrationClient appointmentBackendIntegrationClient) {
        this.appointmentBackendIntegrationClient = appointmentBackendIntegrationClient;

        Button mainViewButton = new Button("BACK TO MAIN PAGE");
        mainViewButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(PropertyListView.class)));

        H1 header = new H1("APPOINTMENTS");
        header.getStyle().set("font-size", "22px");

        List<LinkedHashMap<String, Object>> fetchedAppointments = appointmentBackendIntegrationClient.getAllAppointments();

        for (LinkedHashMap<String, Object> appointment : fetchedAppointments) {
            allAppointments.add(Appointment.builder()
                    .appointmentId(Long.valueOf((Integer) appointment.get("appointmentId")))
                    .dateOfMeeting(LocalDate.parse((String) appointment.get("dateOfMeeting")))
                    .description((String) appointment.get("description"))
                    .propertyId(Long.valueOf((Integer) appointment.get("propertyId")))
                    .userId(Long.valueOf((Integer) appointment.get("userId")))
                    .build());
        }

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
        add(mainViewButton, header, datePicker, appointmentsGrid, cancelButton);
    }

    private void updateGrid(LocalDate selectedDate) {
        List<Appointment> filteredAppointments = allAppointments.stream()
                .filter(appointment -> appointment.getDateOfMeeting().equals(selectedDate))
                .collect(Collectors.toList());
        appointmentsGrid.setItems(filteredAppointments);
    }
}