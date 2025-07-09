package com.vsbm.vehicle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends RepresentationModel<Vehicle> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Brand is required")
    @Size(max = 50, message = "Brand must be at most 50 characters")
    @Column(nullable = false)
    private String brand;

    @NotBlank(message = "Model is required")
    @Size(max = 50, message = "Model must be at most 50 characters")
    @Column(nullable = false)
    private String model;

    @NotBlank(message = "Plate number is required")
    @Pattern(regexp = "^[A-Z0-9-]{5,10}$", message = "Plate number must be 5-10 characters and contain only uppercase letters, digits, or hyphens")
    @Column(nullable = false, unique = true)
    private String plateNumber;

    @NotNull(message = "Client ID is required")
    @Column(name = "client_id", nullable = false)
    private String clientId;
}
