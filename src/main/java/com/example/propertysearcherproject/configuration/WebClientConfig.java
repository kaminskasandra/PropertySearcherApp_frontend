package com.example.propertysearcherproject.configuration;

import com.helger.commons.io.stream.WrappedWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/v1")
                .build();
    }

    @Bean
    public WebClient getExternalCurrencyClient() {
        return WebClient.builder()
                .baseUrl("https://currency-exchange.p.rapidapi.com/exchange")
                .build();
    }

    @Bean
    public WebClient getExternalWeatherClient() {
        return WebClient.builder()
                .baseUrl("https://weatherapi-com.p.rapidapi.com/current.json")
                .build();
    }
}
