package com.appointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto extends RepresentationModel<VehicleDto> {

    private Long id;

    @NotBlank(message = "Brand is required")
    @Size(max = 50, message = "Brand must be at most 50 characters")
    private String brand;

    @NotBlank(message = "Model is required")
    @Size(max = 50, message = "Model must be at most 50 characters")
    private String model;

    @NotBlank(message = "Plate number is required")
    @Pattern(regexp = "^[A-Z0-9-]{5,10}$", message = "Plate number must be 5-10 characters and contain only uppercase letters, digits, or hyphens")
    private String plateNumber;

    private String clientId;
}

