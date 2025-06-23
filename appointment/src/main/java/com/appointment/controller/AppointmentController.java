package com.appointment.controller;

import com.appointment.dto.*;
import com.appointment.service.AppointmentService;
import com.appointment.service.ReceiptServiceProxy;
import com.appointment.service.ServiceTypeServiceProxy;
import com.appointment.service.VehicleServiceProxy;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    private final VehicleServiceProxy vehicleServiceProxy;

    private final ServiceTypeServiceProxy serviceTypeServiceProxy;

    private final ReceiptServiceProxy receiptServiceProxy; // Proxy nou adăugat

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @GetMapping(value= "/list")
    public CollectionModel<AppointmentDto> findAll() {

        List<AppointmentDto> appointments = appointmentService.findAll();
        for (final AppointmentDto appointment : appointments) {
            Link selfLink = linkTo(methodOn(AppointmentController.class).getAppointment(appointment.getId())).withSelfRel();
            appointment.add(selfLink);

            //Link deleteLink = linkTo(methodOn(AppointmentController.class).deleteAppointment(appointment.getId())).withRel("deleteSubscription");
            //appointment.add(deleteLink);

            //Link postLink = linkTo(methodOn(AppointmentController.class).saveAppointment(appointment)).withRel("saveSubscription");
            //appointment.add(postLink);

            //Link putLink = linkTo(methodOn(AppointmentController.class).updateAppointment(appointment)).withRel("updateSubscription");
            //appointment.add(putLink);
        }

        Link link = linkTo(methodOn(AppointmentController.class).findAll()).withSelfRel();
        return CollectionModel.of(appointments, link);
    }

    @PostMapping
    public ResponseEntity<AppointmentDto> saveAppointment(@Valid @RequestBody AppointmentDto appointmentDto){
        AppointmentDto saved = appointmentService.save(appointmentDto);
        URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId())
                .toUri();

        Link selfLink = linkTo(methodOn(AppointmentController.class).getAppointment(saved.getId())).withSelfRel();
        saved.add(selfLink);

        return ResponseEntity.created(locationUri).body(saved);
    }

    @GetMapping("/{id}")
    public AppointmentDto getAppointment(@PathVariable Long id) {
        AppointmentDto appointmentDto = appointmentService.findById(id);

        appointmentDto.setVehicleDto(getVehicleDto(appointmentDto.getVehicleId()));
        appointmentDto.setServiceTypeDtos(getServiceTypeDtos(appointmentDto.getServiceTypeIds()));

        return appointmentDto;
    }

    @CircuitBreaker(name = "vehicleById", fallbackMethod = "vehicleFallback")
    public VehicleDto getVehicleDto(Long vehicleId) {
        return vehicleServiceProxy.getVehicleById(vehicleId).block();
    }

    public VehicleDto vehicleFallback(Long vehicleId, Throwable throwable) {
        logger.warn("Fallback for vehicle ID: {}", vehicleId, throwable);
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
        logger.warn("Fallback for service type list: " + serviceTypeIds, throwable);
        return Collections.emptyList();
    }

    @PostMapping("/{appointmentId}/receipt")
    public ResponseEntity<ReceiptDto> generateReceipt(@PathVariable Long appointmentId) {
        // 1. Obține detaliile programării
        AppointmentDto appointment = appointmentService.findById(appointmentId);

        // 2. Obține detaliile complete de la celelalte microservicii
        VehicleDto vehicle = getVehicleDto(appointment.getVehicleId());
        List<ServiceTypeDto> services = getServiceTypeDtos(appointment.getServiceTypeIds());

        // 3. Calculează totalul și pregătește DTO-ul pentru chitanță
        double totalAmount = services.stream()
                .mapToDouble(ServiceTypeDto::getPrice)
                .sum();

        List<ServiceCopy> serviceCopies = services.stream()
                .map(st -> new ServiceCopy(st.getName(), st.getPrice()))
                .collect(Collectors.toList());

        ReceiptDto receiptToCreate = ReceiptDto.builder()
                .appointmentId(appointment.getId())
                .clientId(appointment.getClientId())
                .mechanicId(appointment.getMechanicId())
                .vehicle(vehicle.getBrand() + " " + vehicle.getModel() + " - " + vehicle.getPlateNumber())
                .services(serviceCopies)
                .totalAmount(totalAmount)
                .appointmentDateTime(appointment.getDateTime())
                .build();


        // 4. Apelează receipt-service pentru a crea chitanța
        ReceiptDto createdReceipt = receiptServiceProxy.createReceipt(receiptToCreate).block(); // ← aici e corectat

        // 5. Actualizează programarea local cu ID-ul chitanței
        appointmentService.markAsReceiptGenerated(appointmentId, createdReceipt.getId());

        return ResponseEntity.ok(createdReceipt);
    }

    @GetMapping("/{appointmentId}/receipt")
    public ResponseEntity<ReceiptDto> viewReceipt(@PathVariable Long appointmentId) {
        // Apelează receipt-service pentru a obține chitanța
        ReceiptDto receipt = receiptServiceProxy.getReceiptByAppointmentId(appointmentId).block(); // ← aici e corectat
        return ResponseEntity.ok(receipt);
    }

}
