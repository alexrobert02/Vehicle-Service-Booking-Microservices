package com.appointment.controller;

import com.appointment.dto.AppointmentDto;
import com.appointment.service.AppointmentService;
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
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    private final VehicleServiceProxy vehicleServiceProxy;

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
    @CircuitBreaker(name="vehicleById", fallbackMethod = "getAppointmentFallback")
    public AppointmentDto getAppointment(@PathVariable Long id) {
        AppointmentDto appointmentDto = appointmentService.findById(id);

        vehicleServiceProxy.getVehicleById(appointmentDto.getVehicleId())
                .doOnNext(vehicleDto -> logger.info(String.valueOf(vehicleDto.getId())))
                .map(vehicleDto -> {
                    appointmentDto.setVehicleDto(vehicleDto);
                    return appointmentDto;
                }).block();
        return appointmentDto;
    }

    AppointmentDto getAppointmentFallback(Long subscriptionId, Throwable throwable) {
        return appointmentService.findById(subscriptionId);
    }
}
