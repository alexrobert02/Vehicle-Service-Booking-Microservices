package com.vsbm.vehicle.repository;

import com.vsbm.vehicle.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByClientId(String id);
    Page<Vehicle> findByClientId(Long id, Pageable pageable);
}
