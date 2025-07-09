package com.vsbm.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("authentication_auth_route", r -> r
                        .path("/vsbm/auth/**")
                        .filters(f -> f
                                .rewritePath("/vsbm/auth(?<segment>/?.*)", "/auth${segment}")
                                .filter((exchange, chain) -> {
                                    long start = System.currentTimeMillis();
                                    return chain.filter(exchange).then(
                                            Mono.fromRunnable(() -> {
                                                long duration = System.currentTimeMillis() - start;
                                                exchange.getResponse().getHeaders().add("X-Response-Time", duration + "ms");
                                            })
                                    );
                                })
                        )
                        .uri("lb://AUTHENTICATION")
                )
                .route("authentication_user_route", r -> r
                        .path("/vsbm/user/**")
                        .filters(f -> f
                                .rewritePath("/vsbm/user(?<segment>/?.*)", "/user${segment}")
                                .filter((exchange, chain) -> {
                                    long start = System.currentTimeMillis();
                                    return chain.filter(exchange).then(
                                            Mono.fromRunnable(() -> {
                                                long duration = System.currentTimeMillis() - start;
                                                exchange.getResponse().getHeaders().add("X-Response-Time", duration + "ms");
                                            })
                                    );
                                })
                        )
                        .uri("lb://AUTHENTICATION")
                )
                .route("vehicle_route", r -> r
                        .path("/vsbm/vehicle/**")
                        .filters(f -> f.rewritePath("/vsbm/vehicle(?<segment>/?.*)", "/vehicle${segment}")
                                .filter((exchange, chain) -> {
                                    long start = System.currentTimeMillis();
                                    return chain.filter(exchange).then(
                                            Mono.fromRunnable(() -> {
                                                long duration = System.currentTimeMillis() - start;
                                                exchange.getResponse().getHeaders().add("X-Response-Time", duration + "ms");
                                            })
                                    );
                                })
                        )
                        .uri("lb://VEHICLE"))
                .route("service-type_route", r -> r
                        .path("/vsbm/service-type/**")
                        .filters(f -> f.rewritePath("/vsbm/service-type(?<segment>/?.*)", "/service-type${segment}")
                                .filter((exchange, chain) -> {
                                    long start = System.currentTimeMillis();
                                    return chain.filter(exchange).then(
                                            Mono.fromRunnable(() -> {
                                                long duration = System.currentTimeMillis() - start;
                                                exchange.getResponse().getHeaders().add("X-Response-Time", duration + "ms");
                                            })
                                    );
                                })
                        )
                        .uri("lb://SERVICE-TYPE"))
                .route("appointment_route", r -> r
                        .path("/vsbm/appointment/**")
                        .filters(f -> f.rewritePath("/vsbm/appointment(?<segment>/?.*)", "/appointment${segment}")
                                .filter((exchange, chain) -> {
                                    long start = System.currentTimeMillis();
                                    return chain.filter(exchange).then(
                                            Mono.fromRunnable(() -> {
                                                long duration = System.currentTimeMillis() - start;
                                                exchange.getResponse().getHeaders().add("X-Response-Time", duration + "ms");
                                            })
                                    );
                                })
                        )
                        .uri("lb://APPOINTMENT"))
                .route("receipt_route", r -> r
                        .path("/vsbm/receipt/**")
                        .filters(f -> f.rewritePath("/vsbm/receipt(?<segment>/?.*)", "/receipt${segment}")
                                .filter((exchange, chain) -> {
                                    long start = System.currentTimeMillis();
                                    return chain.filter(exchange).then(
                                            Mono.fromRunnable(() -> {
                                                long duration = System.currentTimeMillis() - start;
                                                exchange.getResponse().getHeaders().add("X-Response-Time", duration + "ms");
                                            })
                                    );
                                })
                        )
                        .uri("lb://RECEIPT"))
                .build();
    }
}
