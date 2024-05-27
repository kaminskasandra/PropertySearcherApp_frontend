package com.example.propertysearcherproject.integration;


import com.example.propertysearcherproject.configuration.WebClientConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Scope("prototype")
public class WeatherIntegration {
    private final WebClientConfig webClient;

    public String getActualWeather(String city) {
        return webClient.getExternalWeatherClient()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("")
                        .queryParam("q", city).build())
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN)
                .headers(httpHeaders -> {
                    httpHeaders.add("X-RapidAPI-Key", "780886e077msh4799fd66a4aa1fep1cebf9jsn76272b438ff5");
                    httpHeaders.add("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com");
                    httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
