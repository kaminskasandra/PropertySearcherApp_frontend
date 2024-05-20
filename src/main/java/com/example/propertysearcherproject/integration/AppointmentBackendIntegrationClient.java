package com.example.propertysearcherproject.integration;

import com.example.propertysearcherproject.domain.Appointment;
import com.example.propertysearcherproject.configuration.WebClientConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Component
public class AppointmentBackendIntegrationClient {
    private final WebClientConfig webClient;

    public List getAllAppointments() {
        return webClient.getWebClient()
                .get()
                .uri("/appointments")
                .retrieve()
                .bodyToMono(List.class)
                .block();
    }

    public Appointment getAppointmentById(Long id) {
        return webClient.getWebClient()
                .get()
                .uri("/appointments/" + id)
                .retrieve()
                .bodyToMono(Appointment.class)
                .block();
    }

    public Void deleteAppointmentById(Long id) {
        return webClient.getWebClient()
                .delete()
                .uri("/appointments/" + id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public Appointment saveAppointment(Appointment appointment) {
        return webClient.getWebClient()
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(appointment), Appointment.class)
                .retrieve()
                .bodyToMono(Appointment.class)
                .block();
    }

    public Mono<Appointment> updateAppointment(Appointment appointment, Long id) {
        return webClient.getWebClient()
                .put()
                .uri("/appointments/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(appointment), Appointment.class)
                .retrieve()
                .bodyToMono(Appointment.class);
    }
}
