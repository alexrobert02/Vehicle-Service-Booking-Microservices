package com.vsbm.receipt.service;

import com.vsbm.receipt.dto.ServiceTypeDto;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ServiceTypeServiceProxy {

    private final WebClient webClient;

    public ServiceTypeServiceProxy(WebClient.Builder builder) {
        this.webClient = builder
                .filter(new ServletBearerExchangeFilterFunction()) // enables token propagation
                .build();
    }

    public Mono<ServiceTypeDto> getServiceTypeById(Long id) {
        return webClient
                .get()
                .uri("lb://SERVICE-TYPE/service-type/{id}", id)
                .header(HttpHeaders.AUTHORIZATION)
                .retrieve()
                .bodyToMono(ServiceTypeDto.class);
    }
}
