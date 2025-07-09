package com.receipt.controller;

import com.receipt.dto.ReceiptDto;
import com.receipt.service.ReceiptService;
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
@RequestMapping("/receipt")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping("/{appointmentId}")
    public ResponseEntity<ReceiptDto> createReceipt(@PathVariable Long appointmentId) {
        ReceiptDto savedReceipt = receiptService.createReceipt(appointmentId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(savedReceipt.getId()).toUri();

        return ResponseEntity.created(location).body(savedReceipt);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptDto> getReceiptById(@PathVariable Long id) {
        ReceiptDto receipt = receiptService.findById(id);
        addLinks(receipt);
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<ReceiptDto> getReceiptByAppointmentId(@PathVariable Long appointmentId) {
        ReceiptDto receipt = receiptService.findByAppointmentId(appointmentId);
        addLinks(receipt);
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<CollectionModel<ReceiptDto>> getReceiptsByClientId(@PathVariable String clientId) {
        List<ReceiptDto> receipts = receiptService.findByClientId(clientId);
        receipts.forEach(this::addLinks);
        Link selfLink = linkTo(methodOn(ReceiptController.class).getReceiptsByClientId(clientId)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(receipts, selfLink));
    }

    @GetMapping("/mechanic/{mechanicId}")
    public ResponseEntity<CollectionModel<ReceiptDto>> getReceiptsByMechanicId(@PathVariable String mechanicId) {
        List<ReceiptDto> receipts = receiptService.findByMechanicId(mechanicId);
        receipts.forEach(this::addLinks);
        Link selfLink = linkTo(methodOn(ReceiptController.class).getReceiptsByMechanicId(mechanicId)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(receipts, selfLink));
    }

    private void addLinks(ReceiptDto receipt) {
        receipt.add(linkTo(methodOn(ReceiptController.class).getReceiptById(receipt.getId())).withSelfRel());
        receipt.add(linkTo(methodOn(ReceiptController.class).getReceiptByAppointmentId(receipt.getAppointmentId())).withRel("by-appointment-id"));
    }
}
