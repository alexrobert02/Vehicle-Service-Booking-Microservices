package com.vsbm.vehicle.controller;

import com.vsbm.vehicle.dto.VehicleDto;
import com.vsbm.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @GetMapping("/list")
    public CollectionModel<VehicleDto> findAll() {
        List<VehicleDto> vehicles = vehicleService.findAll();

        for (final VehicleDto vehicle : vehicles) {
            Link selfLink = linkTo(methodOn(VehicleController.class).getVehicle(vehicle.getId())).withSelfRel();
            vehicle.add(selfLink);

            Link deleteLink = linkTo(methodOn(VehicleController.class).deleteVehicle(vehicle.getId())).withRel("deleteVehicle");
            vehicle.add(deleteLink);

            Link postLink = linkTo(methodOn(VehicleController.class).saveVehicle(vehicle)).withRel("saveVehicle");
            vehicle.add(postLink);

            Link putLink = linkTo(methodOn(VehicleController.class).updateVehicle(vehicle)).withRel("updateVehicle");
            vehicle.add(putLink);
        }

        Link link = linkTo(methodOn(VehicleController.class).findAll()).withSelfRel();
        return CollectionModel.of(vehicles, link);
    }

    @GetMapping("/{id}")
    public VehicleDto getVehicle(@PathVariable Long id) {
        return vehicleService.findById(id);
    }

    @PreAuthorize("hasRole('user_client')")
    @PostMapping
    public ResponseEntity<VehicleDto> saveVehicle(@Valid @RequestBody VehicleDto vehicleDto) {
        VehicleDto saved = vehicleService.save(vehicleDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();

        Link selfLink = linkTo(methodOn(VehicleController.class).getVehicle(saved.getId())).withSelfRel();
        saved.add(selfLink);

        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping
    public ResponseEntity<VehicleDto> updateVehicle(@Valid @RequestBody VehicleDto vehicleDto) {
        VehicleDto updated = vehicleService.save(vehicleDto);

        Link selfLink = linkTo(methodOn(VehicleController.class).getVehicle(updated.getId())).withSelfRel();
        updated.add(selfLink);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        boolean deleted = vehicleService.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/client/{clientId}")
    public List<VehicleDto> getVehiclesByClientId(@PathVariable String clientId) {
        return vehicleService.findByClientId(clientId);
    }
}

