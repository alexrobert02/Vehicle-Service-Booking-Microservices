package com.receipt.service;

import com.receipt.dto.AppointmentDto;
import com.receipt.dto.ServiceTypeDto;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AppointmentServiceProxy {

    private final WebClient webClient;

    public AppointmentServiceProxy(WebClient.Builder builder) {
        this.webClient = builder
                .filter(new ServletBearerExchangeFilterFunction()) // enables token propagation
                .build();
    }

    public Mono<AppointmentDto> getAppointmentById(Long id) {
        return webClient
                .get()
                .uri("lb://APPOINTMENT/appointment/{id}", id)
                .header(HttpHeaders.AUTHORIZATION)
                .retrieve()
                .bodyToMono(AppointmentDto.class);
    }
}

