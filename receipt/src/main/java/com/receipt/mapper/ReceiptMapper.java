package com.receipt.mapper;

import com.receipt.dto.ReceiptDto;
import com.receipt.entity.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping; // Import necesar

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    // Adăugăm maparea pentru noul câmp
    @Mapping(source = "appointmentDateTime", target = "appointmentDateTime")
    Receipt toReceipt(ReceiptDto dto);

    // Și în direcția opusă
    @Mapping(source = "appointmentDateTime", target = "appointmentDateTime")
    ReceiptDto toReceiptDto(Receipt receipt);
}
