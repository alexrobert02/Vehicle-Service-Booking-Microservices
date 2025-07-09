package com.vsbm.appointment.service;

import com.vsbm.appointment.dto.VehicleDto;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class VehicleServiceProxy {

    private final WebClient webClient;

    public VehicleServiceProxy(WebClient.Builder builder) {
        this.webClient = builder
                .filter(new ServletBearerExchangeFilterFunction()) // enables token propagation
                .build();
    }

    public Mono<VehicleDto> getVehicleById(Long id) {
        return webClient
                .get()
                .uri("lb://VEHICLE/vehicle/{id}", id)
                .header(HttpHeaders.AUTHORIZATION)
                .retrieve()
                .bodyToMono(VehicleDto.class);
    }
}
