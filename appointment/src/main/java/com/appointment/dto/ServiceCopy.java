package com.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Această clasă este o copie a celei din 'receipt-service'.
// Este necesară pentru a construi corect obiectul ReceiptDto.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCopy {
    private String name;
    private Double price;
}
