package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.Appointment;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route("appointments")
public class AppointmentsView extends VerticalLayout {

    private final List<Appointment> allAppointments = new ArrayList<>();
    private final Grid<Appointment> appointmentsGrid = new Grid<>();
    private final DatePicker datePicker = new DatePicker();

    public AppointmentsView() {
        Button mainViewButton = new Button("BACK TO MAIN PAGE");
        mainViewButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(PropertyListView.class)));

        H1 header = new H1("APPOINTMENTS");
        header.getStyle().set("font-size", "22px");

        allAppointments.add(new Appointment(LocalDate.now(), "Meeting with Client 1"));
        allAppointments.add(new Appointment(LocalDate.now().plusDays(1), "Meeting on the building plot"));
        allAppointments.add(new Appointment(LocalDate.now().plusDays(2), "Meeting in apartment in Wrocław"));

        datePicker.setLabel("Select Date");
        datePicker.addValueChangeListener(event -> updateGrid(event.getValue()));

        appointmentsGrid.addColumn(Appointment::getDescription).setHeader("MEETING DESCRIPTION");
        appointmentsGrid.addColumn(Appointment::getDateOfMeeting).setHeader("DATE OF MEETING");

        appointmentsGrid.setItems(allAppointments);

        Button cancelButton = new Button("Cancel Appointment");
        cancelButton.setEnabled(false);

        appointmentsGrid.asSingleSelect().addValueChangeListener(event ->
                cancelButton.setEnabled(event.getValue() != null));

        cancelButton.addClickListener(event -> {
            Appointment selectedAppointment = appointmentsGrid.asSingleSelect().getValue();
            if (selectedAppointment != null) {
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