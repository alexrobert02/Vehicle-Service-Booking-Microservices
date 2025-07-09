package com.vsbm.receipt.service;

import com.vsbm.receipt.dto.AppointmentDto;
import com.vsbm.receipt.dto.ReceiptDto;
import com.vsbm.receipt.dto.ServiceTypeDto;
import com.vsbm.receipt.dto.VehicleDto;
import com.vsbm.receipt.entity.Receipt;
import com.vsbm.receipt.entity.ServiceCopy;
import com.vsbm.receipt.exception.ReceiptNotFoundException;
import com.vsbm.receipt.mapper.ReceiptMapper;
import com.vsbm.receipt.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    private final ReceiptMapper receiptMapper;

    private final AppointmentServiceProxy appointmentServiceProxy;

    private final VehicleServiceProxy vehicleServiceProxy;

    private final ServiceTypeServiceProxy serviceTypeServiceProxy;

    private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);

    @Transactional
    public ReceiptDto createReceipt(Long appointmentId) {
        AppointmentDto appointment = getAppointmentDto(appointmentId);
        VehicleDto vehicle = getVehicleDto(appointment.getVehicleId());
        List<ServiceTypeDto> services = getServiceTypeDtos(appointment.getServiceTypeIds());

        double totalAmount = services.stream().mapToDouble(ServiceTypeDto::getPrice).sum();

        List<ServiceCopy> serviceCopies = services.stream()
                .map(st -> new ServiceCopy(st.getName(), st.getPrice()))
                .collect(Collectors.toList());

        ReceiptDto receiptDto = ReceiptDto.builder()
                .appointmentId(appointment.getId())
                .clientId(appointment.getClientId())
                .mechanicId(appointment.getMechanicId())
                .vehicle(vehicle.getBrand() + " " + vehicle.getModel() + " - " + vehicle.getPlateNumber())
                .services(serviceCopies)
                .totalAmount(totalAmount)
                .appointmentDateTime(appointment.getDateTime())
                .issueDate(LocalDate.now())
                .build();

        Receipt saved = receiptRepository.save(receiptMapper.toReceipt(receiptDto));
        return receiptMapper.toReceiptDto(saved);
    }

    @CircuitBreaker(name = "vehicleById", fallbackMethod = "vehicleFallback")
    public VehicleDto getVehicleDto(Long vehicleId) {
        return vehicleServiceProxy.getVehicleById(vehicleId).block();
    }

    public VehicleDto vehicleFallback(Long vehicleId, Throwable throwable) {
        logger.warn("Fallback for vehicle ID: {}", vehicleId, throwable);
        return null;
    }

    @CircuitBreaker(name = "appointmentById", fallbackMethod = "appointmentFallback")
    public AppointmentDto getAppointmentDto(Long appointmentId) {
        return appointmentServiceProxy.getAppointmentById(appointmentId).block();
    }

    public AppointmentDto appointmentFallback(Long appointmentId, Throwable throwable) {
        logger.warn("Fallback for appointment ID: {}", appointmentId, throwable);
        return null;
    }

    @CircuitBreaker(name = "serviceTypes", fallbackMethod = "serviceTypesFallback")
    public List<ServiceTypeDto> getServiceTypeDtos(List<Long> serviceTypeIds) {
        return serviceTypeIds.stream()
                .map(id -> {
                    try {
                        return serviceTypeServiceProxy.getServiceTypeById(id).block();
                    } catch (Exception e) {
                        logger.warn("Fallback for service type ID: {}", id, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public List<ServiceTypeDto> serviceTypesFallback(List<Long> serviceTypeIds, Throwable throwable) {
        logger.warn("Fallback for service type list: {}", serviceTypeIds, throwable);
        return Collections.emptyList();
    }

    public ReceiptDto findById(Long id) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new ReceiptNotFoundException("Receipt not found with id: " + id));
        return receiptMapper.toReceiptDto(receipt);
    }

    public ReceiptDto findByAppointmentId(Long appointmentId) {
        Receipt receipt = receiptRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ReceiptNotFoundException("Receipt not found for appointment id: " + appointmentId));
        return receiptMapper.toReceiptDto(receipt);
    }

    public List<ReceiptDto> findByClientId(String clientId) {
        return receiptRepository.findByClientId(clientId).stream()
                .map(receiptMapper::toReceiptDto)
                .collect(Collectors.toList());
    }

    public List<ReceiptDto> findByMechanicId(String mechanicId) {
        return receiptRepository.findByMechanicId(mechanicId).stream()
                .map(receiptMapper::toReceiptDto)
                .collect(Collectors.toList());
    }
}
