package com.example.propertysearcherproject.integration;

import com.example.propertysearcherproject.domain.User;
import com.example.propertysearcherproject.configuration.WebClientConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Component
public class UserBackendIntegrationClient {
    private final WebClientConfig webClient;

    public List getAllUsers() {
        return webClient.getWebClient()
                .get()
                .uri("/users")
                .retrieve()
                .bodyToMono(List.class)
                .block();
    }

    public User getUserById(Long id) {
        return webClient.getWebClient()
                .get()
                .uri("/users/" + id)
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }

    public Void deleteUserById(Long id) {
        return webClient.getWebClient()
                .delete()
                .uri("/users/" + id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public User saveUser(User user) {
        return webClient.getWebClient()
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }

    public Mono<User> updateUser(User user, Long userId) {
        return webClient.getWebClient()
                .put()
                .uri("/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .retrieve()
                .bodyToMono(User.class);
    }
}
