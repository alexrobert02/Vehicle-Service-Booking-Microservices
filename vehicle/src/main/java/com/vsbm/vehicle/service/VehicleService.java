package com.vsbm.vehicle.service;

import com.vsbm.vehicle.dto.VehicleDto;
import com.vsbm.vehicle.entity.Vehicle;
import com.vsbm.vehicle.exception.VehicleNotFound;
import com.vsbm.vehicle.mapper.VehicleMapper;
import com.vsbm.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public List<VehicleDto> findAll() {
        return vehicleRepository.findAll()
                .stream()
                .map(vehicleMapper::toVehicleDto)
                .toList();
    }

    public VehicleDto findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFound("Vehicle not found with id: " + id));
        return vehicleMapper.toVehicleDto(vehicle);
    }

    @Transactional
    public VehicleDto save(VehicleDto vehicleDto) {
        String clientId = SecurityUtil.getUserId();
        System.out.println("clientId: " + clientId);
        System.out.println("vehicleDto: " + vehicleDto.getBrand() + vehicleDto.getModel() + vehicleDto.getPlateNumber());
        vehicleDto.setClientId(clientId);
        Vehicle vehicle = vehicleMapper.toVehicle(vehicleDto);
        System.out.println("vehicle: " + vehicle.getBrand() + vehicle.getModel() + vehicle.getPlateNumber() + vehicle.getClientId());
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toVehicleDto(savedVehicle);
    }

    public boolean deleteById(Long id) {
        if (!vehicleRepository.existsById(id)) {
            return false;
        }
        vehicleRepository.deleteById(id);
        return true;
    }

    public List<VehicleDto> findByClientUsername(String username) {
        String clientId = SecurityUtil.getUserId();
        return vehicleRepository.findByClientId(clientId)
                .stream()
                .map(vehicleMapper::toVehicleDto)
                .toList();
    }
}

