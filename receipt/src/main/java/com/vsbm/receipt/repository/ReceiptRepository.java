package com.vsbm.receipt.repository;

import com.vsbm.receipt.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByAppointmentId(Long appointmentId);
    List<Receipt> findByClientId(String clientId);
    List<Receipt> findByMechanicId(String mechanicId);
}
