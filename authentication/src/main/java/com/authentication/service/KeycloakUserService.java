package com.authentication.service;

import com.authentication.dto.LoginRequest;
import com.authentication.dto.RegisterRequest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class KeycloakUserService {

    private final WebClient webClient = WebClient.builder().build();
    private final ClientRegistration keycloakRegistration;

    public KeycloakUserService(ClientRegistrationRepository repo) {
        this.keycloakRegistration = repo.findByRegistrationId("spring-with-test-scope");
    }

    public Map login(LoginRequest request) {
        String tokenUri = "http://localhost:8080/realms/awbd/protocol/openid-connect/token";

        return webClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("grant_type", "password")
                        .with("client_id", keycloakRegistration.getClientId())
                        .with("client_secret", keycloakRegistration.getClientSecret())
                        .with("username", request.getUsername())
                        .with("password", request.getPassword()))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public void registerUser(RegisterRequest request) {
        String token = getAccessToken().block();

        System.out.println(token);

        Map<String, Object> userPayload = Map.of(
                "username", request.getUsername(),
                "enabled", true,
                "credentials", List.of(
                        Map.of(
                                "type", "password",
                                "value", request.getPassword(),
                                "temporary", false
                        )
                )
        );

        webClient.post()
                .uri("http://localhost:8080/admin/realms/awbd/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userPayload)
                .retrieve()
                .toBodilessEntity()
                .block();

        Map roleRepresentation = webClient.get()
                .uri("http://localhost:8080/admin/realms/awbd/roles/{roleName}", request.getRole().name().toLowerCase())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String userId = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8080)
                        .path("/admin/realms/awbd/users")
                        .queryParam("username", request.getUsername())
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(Map.class)
                .blockFirst()
                .get("id").toString();

        webClient.post()
                .uri("http://localhost:8080/admin/realms/awbd/users/{userId}/role-mappings/realm", userId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(List.of(roleRepresentation))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private Mono<String> getAccessToken() {
        String tokenUri = "http://localhost:8080/realms/awbd/protocol/openid-connect/token";
        String clientId = keycloakRegistration.getClientId();
        String clientSecret = keycloakRegistration.getClientSecret();

        return webClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("grant_type", "client_credentials")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret))
                .retrieve()
                .bodyToMono(Map.class)
                .map(resp -> (String) resp.get("access_token"));
    }
}

