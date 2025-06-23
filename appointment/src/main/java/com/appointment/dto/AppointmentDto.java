package com.appointment.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto extends RepresentationModel<AppointmentDto> {

    private Long id;

    @NotNull(message = "Appointment date and time is required")
    @FutureOrPresent(message = "Appointment date and time must be present or future")
    private LocalDateTime dateTime;

    private String clientId;

    @NotNull(message = "Mechanic ID is required")
    private String mechanicId;

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    private VehicleDto vehicleDto;

    @NotEmpty(message = "At least one service type ID is required")
    private List<Long> serviceTypeIds;
}
