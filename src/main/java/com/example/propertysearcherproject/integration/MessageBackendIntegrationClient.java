package com.example.propertysearcherproject.integration;

import com.example.propertysearcherproject.configuration.WebClientConfig;
import com.example.propertysearcherproject.domain.Message;
import com.example.propertysearcherproject.domain.Property;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Component
@Scope("prototype")
public class MessageBackendIntegrationClient {
    private final WebClientConfig webClient;

    public List getAllMessages(long userId) {
        return webClient.getWebClient()
                .get()
                .uri(uriBuilder -> uriBuilder.path("/message/findAllByUserId")
                        .queryParam("userId", userId)
                        .build())
                .retrieve()
                .bodyToMono(List.class)
                .block();
    }

    public Message getMessageById(Long messageId) {
        Message message = webClient.getWebClient()
                .get()
                .uri("/message/" + messageId)
                .retrieve()
                .bodyToMono(Message.class)
                .block();
        return message;
    }

    public Void deleteMessageById(Long id) {
        return webClient.getWebClient()
                .delete()
                .uri("/message/" + id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public Property saveMessage(Message message) {
        return webClient.getWebClient()
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(message), Message.class)
                .retrieve()
                .bodyToMono(Property.class)
                .block();
    }
}
