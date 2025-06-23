package com.servicetype.controller;

import com.servicetype.dto.ServiceTypeDto;
import com.servicetype.service.ServiceTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/service-type")
@RequiredArgsConstructor
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    @GetMapping("/list")
    public CollectionModel<ServiceTypeDto> findAll() {
        List<ServiceTypeDto> serviceTypes = serviceTypeService.findAll();

        for (final ServiceTypeDto serviceType : serviceTypes) {
            Link selfLink = linkTo(methodOn(ServiceTypeController.class).getServiceType(serviceType.getId())).withSelfRel();
            serviceType.add(selfLink);
        }

        Link link = linkTo(methodOn(ServiceTypeController.class).findAll()).withSelfRel();
        return CollectionModel.of(serviceTypes, link);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceTypeDto> getServiceType(@PathVariable Long id) {
        ServiceTypeDto serviceTypeDto = serviceTypeService.findById(id);

        Link selfLink = linkTo(methodOn(ServiceTypeController.class).getServiceType(id)).withSelfRel();
        serviceTypeDto.add(selfLink);

        Link deleteLink = linkTo(methodOn(ServiceTypeController.class).deleteServiceType(id)).withRel("deleteServiceType");
        serviceTypeDto.add(deleteLink);

        return ResponseEntity.ok(serviceTypeDto);
    }

    @PostMapping
    public ResponseEntity<ServiceTypeDto> saveServiceType(@Valid @RequestBody ServiceTypeDto serviceTypeDto) {
        ServiceTypeDto saved = serviceTypeService.save(serviceTypeDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();

        Link selfLink = linkTo(methodOn(ServiceTypeController.class).getServiceType(saved.getId())).withSelfRel();
        saved.add(selfLink);

        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping
    public ResponseEntity<ServiceTypeDto> updateServiceType(@Valid @RequestBody ServiceTypeDto serviceTypeDto) {
        ServiceTypeDto updated = serviceTypeService.save(serviceTypeDto);

        Link selfLink = linkTo(methodOn(ServiceTypeController.class).getServiceType(updated.getId())).withSelfRel();
        updated.add(selfLink);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceType(@PathVariable Long id) {
        serviceTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mechanic/{mechanicId}")
    public ResponseEntity<List<ServiceTypeDto>> getServiceTypesByMechanicId(@PathVariable String mechanicId) {
        List<ServiceTypeDto> serviceTypes = serviceTypeService.findByMechanicId(mechanicId);
        return ResponseEntity.ok(serviceTypes);
    }
}
