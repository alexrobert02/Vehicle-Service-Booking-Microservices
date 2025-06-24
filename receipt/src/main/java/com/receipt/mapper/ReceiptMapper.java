package com.receipt.mapper;

import com.receipt.dto.ReceiptDto;
import com.receipt.entity.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping; // Import necesar

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    //@Mapping(source = "appointmentDateTime", target = "appointmentDateTime")
    Receipt toReceipt(ReceiptDto dto);

    //@Mapping(source = "appointmentDateTime", target = "appointmentDateTime")
    ReceiptDto toReceiptDto(Receipt receipt);
}
