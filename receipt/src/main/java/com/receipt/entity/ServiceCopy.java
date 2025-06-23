package com.receipt.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Clasa ServiceCopy este stocată direct în tabelul 'receipt_services'
// ca parte a entității Receipt, datorită adnotării @Embeddable.
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCopy {
    private String name;
    private Double price;
}
