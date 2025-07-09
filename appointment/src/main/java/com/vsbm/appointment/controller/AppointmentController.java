package com.vsbm.appointment.controller;

import com.appointment.dto.*;
import com.vsbm.appointment.service.AppointmentService;
import com.vsbm.appointment.service.ServiceTypeServiceProxy;
import com.vsbm.appointment.service.VehicleServiceProxy;
import com.vsbm.appointment.dto.AppointmentDto;
import com.vsbm.appointment.dto.ServiceTypeDto;
import com.vsbm.appointment.dto.VehicleDto;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    private final VehicleServiceProxy vehicleServiceProxy;

    private final ServiceTypeServiceProxy serviceTypeServiceProxy;

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @GetMapping(value= "/list")
    public CollectionModel<AppointmentDto> findAll() {

        List<AppointmentDto> appointments = appointmentService.findAll();
        for (final AppointmentDto appointment : appointments) {
            Link selfLink = linkTo(methodOn(AppointmentController.class).getAppointment(appointment.getId())).withSelfRel();
            appointment.add(selfLink);

            Link postLink = linkTo(methodOn(AppointmentController.class).saveAppointment(appointment)).withRel("saveSubscription");
            appointment.add(postLink);
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
        logger.warn("Fallback for service type list: {}", serviceTypeIds, throwable);
        return Collections.emptyList();
    }
}
