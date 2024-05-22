package com.example.propertysearcherproject.views;

import com.example.propertysearcherproject.domain.Message;
import com.example.propertysearcherproject.domain.MessageCategory;
import com.example.propertysearcherproject.domain.User;
import com.example.propertysearcherproject.integration.MessageBackendIntegrationClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Route("messages")
@org.springframework.stereotype.Component
@Scope("prototype")
public class MessagesView extends VerticalLayout {
    private final List<Message> allMessages = new ArrayList<>();
    private final MessageBackendIntegrationClient messageBackendIntegrationClient;
    private final Grid<Message> messagesGrid = new Grid<>();
    private final DatePicker datePicker = new DatePicker();
    private boolean deleteColumnAdded = false;

    public MessagesView(MessageBackendIntegrationClient messageBackendIntegrationClient) {

        this.messageBackendIntegrationClient = messageBackendIntegrationClient;

        Button mainViewButton = new Button("BACK TO MAIN PAGE");
        mainViewButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(PropertyListView.class)));

        H1 header = new H1("MESSAGES");
        header.getStyle().set("font-size", "24px");

        User user = null;
        try {
            user = (User) VaadinSession.getCurrent().getAttribute("username");
        } catch (Exception ignored) {

        }
        List<LinkedHashMap<String, Object>> fetchedMessages = messageBackendIntegrationClient.getAllMessages(user.getUserId());

        for (LinkedHashMap<String, Object> message : fetchedMessages) {
            allMessages.add(Message.builder()
                    .text((String) message.get("text"))
                    .messageCategory(MessageCategory.valueOf((String) message.get("messageCategory")))
                    .date(LocalDate.parse((CharSequence) message.get("date"))).build());
        }

        datePicker.setLabel("Select Date");
        datePicker.addValueChangeListener(event -> updateGrid(event.getValue()));

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
        add(mainViewButton, header);
        addNewMessageForm();
        add(datePicker, buttonsLayout, messagesGrid);
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
                    Message selectedMessage = message;
                    if (selectedMessage != null) {
                        deleteMessage(selectedMessage.getMessageId());
                        allMessages.remove(selectedMessage);
                        filterMessages(MessageCategory.RECEIVED);
                        Notification.show("Message deleted");
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
                .filter(message -> message.getDate().isEqual(selectedDate))
                .collect(Collectors.toList());
        messagesGrid.setItems(filteredMessages);
    }

    private void deleteMessage(Long messageId) {
        messageBackendIntegrationClient.deleteMessageById(messageId);
    }
    private void addNewMessageForm() {
        H2 newMessageHeader = new H2("Create new message");
        newMessageHeader.getStyle().set("font-size", "20px").set("font-weight", "bold");

        TextField userEmailField = new TextField("Email");
        TextArea messageTextArea = new TextArea("Message");
        messageTextArea.setWidthFull();

        Button sendButton = new Button("Send");

        sendButton.addClickListener(event -> {
            String userEmail = userEmailField.getValue();
            String messageText = messageTextArea.getValue();
            if (!userEmail.isEmpty() && !messageText.isEmpty()) {
                Message newMessage = Message.builder()
                        .text(messageText)
                        .messageCategory(MessageCategory.SENT)
                        .date(LocalDate.now())
                        .build();
                messageBackendIntegrationClient.saveMessage(newMessage);
                allMessages.add(newMessage);
                filterMessages(MessageCategory.SENT);
                Notification.show("Message sent");
                userEmailField.clear();
                messageTextArea.clear();
            } else {
                Notification.show("Please fill in all fields", 3000, Notification.Position.MIDDLE);
            }
        });

        VerticalLayout newMessageForm = new VerticalLayout(newMessageHeader, userEmailField, messageTextArea, sendButton);
        newMessageForm.setPadding(false);
        newMessageForm.setSpacing(true);

        add(newMessageForm);
    }
}
