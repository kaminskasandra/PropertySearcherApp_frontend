package com.example.propertysearcherproject.integration;


import com.example.propertysearcherproject.configuration.WebClientConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Scope("prototype")
public class CurrencyExchangeIntegration {
    private final WebClientConfig webClient;

    public String getCurrencyRatio() {
        return webClient.getExternalCurrencyClient()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("")
                        .queryParam("from", "PLN").queryParam("to", "EUR").build())
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN)
                .headers(httpHeaders -> {
                    httpHeaders.add("X-RapidAPI-Key", "780886e077msh4799fd66a4aa1fep1cebf9jsn76272b438ff5");
                    httpHeaders.add("X-RapidAPI-Host", "currency-exchange.p.rapidapi.com");
                    httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
