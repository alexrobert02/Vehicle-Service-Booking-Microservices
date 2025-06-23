package com.receipt.service;

import com.receipt.dto.ReceiptDto;
import com.receipt.entity.Receipt;
import com.receipt.exception.ReceiptNotFoundException;
import com.receipt.mapper.ReceiptMapper;
import com.receipt.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;

    @Transactional
    public ReceiptDto createReceipt(ReceiptDto receiptDto) {
        // Setează data emiterii la momentul creării
        receiptDto.setIssueDate(LocalDate.now());

        Receipt receipt = receiptMapper.toReceipt(receiptDto);
        Receipt savedReceipt = receiptRepository.save(receipt);
        return receiptMapper.toReceiptDto(savedReceipt);
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
