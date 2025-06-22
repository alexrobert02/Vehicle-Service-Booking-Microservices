package com.awbd.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.util.Date;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("subscription_route", r -> r
                        .path("/awbd/subscription/**")
                        .filters(f -> f.rewritePath("/awbd/subscription/(?<segment>.*)", "/${segment}")
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
                        .uri("lb://FIRST-SERVICE"))
                .route("authentication_auth_route", r -> r
                        .path("/awbd/auth/**")
                        .filters(f -> f
                                .rewritePath("/awbd/auth(?<segment>/?.*)", "/auth${segment}")
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
                        .path("/awbd/user/**")
                        .filters(f -> f
                                .rewritePath("/awbd/user(?<segment>/?.*)", "/user${segment}")
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
                        .path("/awbd/vehicle/**")
                        .filters(f -> f.rewritePath("/awbd/vehicle(?<segment>/?.*)", "/vehicle${segment}")
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
                .route("appointment_route", r -> r
                        .path("/awbd/appointment/**")
                        .filters(f -> f.rewritePath("/awbd/appointment(?<segment>/?.*)", "/appointment${segment}")
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
                .route("discount_route", r -> r
                        .path("/awbd/discount/**")
                        .filters(f -> f.rewritePath("/awbd/discount/(?<segment>.*)", "/${segment}")
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
                        .uri("lb://DISCOUNT"))

                .build();
    }
}
