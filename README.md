# Vehicle-Service-Booking-Microservices

This project is a microservices-based version of the previous monolithic application:\
[Vehicle-Service-Booking (Monolith)](https://github.com/alexrobert02/Vehicle-Service-Booking)

## Microservices Overview

| Service                  | Description                                   |
|--------------------------|-----------------------------------------------|
| **API Gateway**          | Central entry point for all client calls      |
| **Config Server**        | Centralized configuration for all services    |
| **Eureka Server**        | Service registry and discovery mechanism      |
| **Auth Service**         | Manages user registration/login via Keycloak  |
| **Vehicle Service**      | Manages user-owned vehicles                   |
| **Service-Type Service** | Manages types of car services                 |
| **Appointment Service**  | Booking and managing appointments             |
| **Receipt Service**      | Generates receipts for completed appointments |

## Architectural Highlights
* **Central Config** — All services read config from `Spring Cloud Config Server`
* **Service Discovery** — Dynamic resolution via **Eureka** using `lb://SERVICE-NAME`
* **Load Balancing** — Enabled with **Spring Cloud LoadBalancer**
* **Secure Communication** — JWT tokens auto-propagated with `ServletBearerExchangeFilterFunction`
* **Monitoring** — Tracing via `Zipkin`, logs via `SLF4J`
* **Authentication** — OAuth2 JWT-based auth using `Keycloak` with role-based access (`CLIENT`, `MECHANIC`)
* **Resilience** — Fallbacks with `@CircuitBreaker` from **Resilience4j**
* **Inter-Service Communication** — Implemented using `WebClient` with reactive, non-blocking HTTP calls
* **Hypermedia Controls** — REST responses enriched using **Spring HATEOAS** to provide discoverability and navigable links for resources

## Running Locally
* **Config Server**
* **Eureka Server**
* **Keycloak** (`docker run -d --name keycloak -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:23.0.7 start-dev`)
* **Zipkin** (`docker run -d -p 9411:9411 openzipkin/zipkin`)
* **Microservices** (`authentication`, `vehicle`, `service-type`, `appointment`, `receipt`)
* **API Gateway**
