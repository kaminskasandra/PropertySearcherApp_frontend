package com.example.propertysearcherproject.integration;

import com.example.propertysearcherproject.domain.Property;
import com.example.propertysearcherproject.configuration.WebClientConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.reflections.Reflections.log;

@AllArgsConstructor
@Component
@Scope("prototype")
public class PropertyBackendIntegrationClient {
    private final WebClientConfig webClient;

    public List getAllProperty() {
        return webClient.getWebClient()
                .get()
                .uri("/property")
                .retrieve()
                .bodyToMono(List.class)
                .block();
    }

    public Property getPropertyById(Long propertyId) {
        Property property = webClient.getWebClient()
                .get()
                .uri("/property/" + propertyId)
                .retrieve()
                .bodyToMono(Property.class)
                .block();
        log.debug("Received property: {}", property);
        return property;
    }

    public Void deletePropertyById(Long id) {
        return webClient.getWebClient()
                .delete()
                .uri("/property/" + id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public Property saveProperty(Property property) {
        return webClient.getWebClient()
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(property), Property.class)
                .retrieve()
                .bodyToMono(Property.class)
                .block();
    }

    public Mono<Property> updateProperty(Property property, Long propertyId) {
        return webClient.getWebClient()
                .put()
                .uri("/property/" + propertyId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(property), Property.class)
                .retrieve()
                .bodyToMono(Property.class);
    }
    public List<Property> getPropertiesByUser(Long userId) {
        return webClient.getWebClient()
                .get()
                .uri("/property/user/{userId}", userId)
                .retrieve()
                .bodyToFlux(Property.class)
                .collectList()
                .block();
    }
}
