package com.receipt.mapper;

import com.receipt.dto.ReceiptDto;
import com.receipt.entity.Receipt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {
    Receipt toReceipt(ReceiptDto dto);
    ReceiptDto toReceiptDto(Receipt receipt);
}
