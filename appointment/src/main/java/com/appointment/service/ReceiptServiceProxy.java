package com.appointment.service;

import com.appointment.dto.ReceiptDto;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ReceiptServiceProxy {

    private final WebClient webClient;

    public ReceiptServiceProxy(WebClient.Builder builder) {
        this.webClient = builder
                .filter(new ServletBearerExchangeFilterFunction()) // enables token propagation
                .build();
    }

    public Mono<ReceiptDto> createReceipt(ReceiptDto receiptDto) {
        return webClient
                .post()
                .uri("lb://RECEIPT/api/receipts")
                .header(HttpHeaders.AUTHORIZATION)
                .bodyValue(receiptDto)
                .retrieve()
                .bodyToMono(ReceiptDto.class);
    }

    public Mono<ReceiptDto> getReceiptByAppointmentId(Long appointmentId) {
        return webClient
                .get()
                .uri("lb://RECEIPT/api/receipts/appointment/{appointmentId}", appointmentId)
                .header(HttpHeaders.AUTHORIZATION)
                .retrieve()
                .bodyToMono(ReceiptDto.class);
    }
}
