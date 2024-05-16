package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.Message;
import com.example.propertysearcherproject.MessageCategory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route("messages")
public class MessagesView extends VerticalLayout {

    private final List<Message> allMessages = new ArrayList<>();
    private final Grid<Message> messagesGrid = new Grid<>();
    private final DatePicker datePicker = new DatePicker();
    private boolean deleteColumnAdded = false;

    public MessagesView() {

        Button mainViewButton = new Button("BACK TO MAIN PAGE");
        mainViewButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(PropertyListView.class)));

        H1 header = new H1("MESSAGES");
        header.getStyle().set("font-size", "24px");

        allMessages.add(new Message("sender@example.com", "Message 1", LocalDate.now(), MessageCategory.SENT));
        allMessages.add(new Message("recipient@example.com", "Message 2", LocalDate.now(), MessageCategory.RECEIVED));
        allMessages.add(new Message("recipient@example.com", "Message 4", LocalDate.now().minusDays(1), MessageCategory.RECEIVED));
        allMessages.add(new Message("recipient@example.com", "Message 5", LocalDate.now().minusDays(3), MessageCategory.RECEIVED));
        allMessages.add(new Message("recipient@example.com", "Message 6", LocalDate.now().minusDays(5), MessageCategory.RECEIVED));
        allMessages.add(new Message("sender@example.com", "Message 3", LocalDate.now(), MessageCategory.DELETED));

        datePicker.setLabel("Select Date");
        datePicker.addValueChangeListener(event -> updateGrid(event.getValue()));

        messagesGrid.addColumn(Message::getEmail).setHeader("Email");
        messagesGrid.addColumn(Message::getText).setHeader("Message Text");
        messagesGrid.addColumn(Message::getDate).setHeader("Date");

        filterMessages(MessageCategory.RECEIVED);

        Button sentButton = new Button("Sent");
        sentButton.addClickListener(event -> filterMessages(MessageCategory.SENT));

        Button receivedButton = new Button("Received");
        receivedButton.addClickListener(event -> filterMessages(MessageCategory.RECEIVED));

        Button deleteButton = new Button("Deleted");
        deleteButton.addClickListener(event -> filterMessages(MessageCategory.DELETED));

        HorizontalLayout buttonsLayout = new HorizontalLayout(sentButton, receivedButton, deleteButton);
        add(mainViewButton, header, datePicker, buttonsLayout, messagesGrid);
    }

    private void filterMessages(MessageCategory category) {
        List<Message> filteredMessages = allMessages.stream()
                .filter(message -> message.getMessageCategory() == category)
                .collect(Collectors.toList());
        messagesGrid.setItems(filteredMessages);

        if (category == MessageCategory.RECEIVED && !deleteColumnAdded) {
            messagesGrid.addComponentColumn(message -> {
                Button deleteButton = new Button("Delete");
                deleteButton.addClickListener(event -> {
                    Message selectedMessage = messagesGrid.asSingleSelect().getValue();
                    if (selectedMessage != null) {
                        selectedMessage.setMessageCategory(MessageCategory.DELETED);
                        filterMessages(MessageCategory.RECEIVED);
                    }
                });
                return deleteButton;
            }).setHeader("Actions");
            deleteColumnAdded = true;
        } else if (category != MessageCategory.RECEIVED && deleteColumnAdded) {
            messagesGrid.removeColumnByKey("Actions");
            deleteColumnAdded = false;
        }
    }

    private void updateGrid(LocalDate selectedDate) {
        List<Message> filteredMessages = allMessages.stream()
                .filter(message -> message.getMessageCategory() == MessageCategory.RECEIVED)
                .collect(Collectors.toList());
        messagesGrid.setItems(filteredMessages);
    }
}