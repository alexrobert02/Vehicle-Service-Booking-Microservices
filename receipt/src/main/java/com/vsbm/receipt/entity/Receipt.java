package com.vsbm.receipt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receipt extends RepresentationModel<Receipt> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDate issueDate;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double totalAmount;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String vehicle;

    @NotNull
    @Column(nullable = false)
    private String clientId;

    @NotNull
    @Column(nullable = false)
    private String mechanicId;

    @NotNull
    @Column(nullable = false, unique = true)
    private Long appointmentId;

    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "receipt_services", joinColumns = @JoinColumn(name = "receipt_id"))
    private List<ServiceCopy> services = new ArrayList<>();
}
