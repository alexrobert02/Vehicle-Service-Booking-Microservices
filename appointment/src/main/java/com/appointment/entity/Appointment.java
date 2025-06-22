package com.appointment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment extends RepresentationModel<Appointment> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Appointment date and time is required")
    @Column(nullable = false)
    private LocalDateTime dateTime;

    @NotNull(message = "Client is required")
    @Column(name = "client_id", nullable = false)
    private String clientId;

    @NotNull(message = "Mechanic is required")
    @Column(name = "mechanic_id", nullable = false)
    private String mechanicId;

    @NotNull(message = "Vehicle is required")
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @NotNull(message = "At least one service type is required")
    @ElementCollection
    @CollectionTable(
            name = "appointment_service_type",
            joinColumns = @JoinColumn(name = "appointment_id")
    )
    @Column(name = "service_type_id", nullable = false)
    private List<Long> serviceTypeIds = new ArrayList<>();

    @ToString.Exclude
    @Column(name = "receipt_id")
    private Long receiptId;
}

