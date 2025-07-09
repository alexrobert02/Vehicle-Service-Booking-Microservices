package com.vsbm.receipt.dto;

import com.vsbm.receipt.entity.ServiceCopy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDto extends RepresentationModel<ReceiptDto> {
    private Long id;
    private LocalDate issueDate;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    private Double totalAmount;

    @NotBlank(message = "Vehicle info is required")
    private String vehicle;

    @NotNull(message = "Client ID is required")
    private String clientId;

    @NotNull(message = "Mechanic ID is required")
    private String mechanicId;

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;
    private LocalDateTime appointmentDateTime;

    @NotEmpty(message = "Services list cannot be empty")
    private List<ServiceCopy> services;
}
