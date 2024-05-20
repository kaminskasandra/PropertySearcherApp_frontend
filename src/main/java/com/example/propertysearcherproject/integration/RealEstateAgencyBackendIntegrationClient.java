package com.example.propertysearcherproject.integration;

import com.example.propertysearcherproject.domain.RealEstateAgency;
import com.example.propertysearcherproject.configuration.WebClientConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Component
public class RealEstateAgencyBackendIntegrationClient {
    private final WebClientConfig webClient;

    public List getAllAgencies() {
        return webClient.getWebClient()
                .get()
                .uri("/agencies/")
                .retrieve()
                .bodyToMono(List.class)
                .block();
    }

    public RealEstateAgency getAgencyById(Long id) {
        return webClient.getWebClient()
                .get()
                .uri("/agencies/" + id)
                .retrieve()
                .bodyToMono(RealEstateAgency.class)
                .block();
    }

    public Void deleteAgencyById(Long id) {
        return webClient.getWebClient()
                .delete()
                .uri("/agencies/" + id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public RealEstateAgency saveAgency(RealEstateAgency realEstateAgency) {
        return webClient.getWebClient()
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(realEstateAgency), RealEstateAgency.class)
                .retrieve()
                .bodyToMono(RealEstateAgency.class)
                .block();
    }

    public Mono<RealEstateAgency> updateAgency(RealEstateAgency realEstateAgency, Long agencyId) {
        return webClient.getWebClient()
                .put()
                .uri("/agencies/" + agencyId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(realEstateAgency), RealEstateAgency.class)
                .retrieve()
                .bodyToMono(RealEstateAgency.class);
    }
}
