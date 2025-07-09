package com.vsbm.receipt.mapper;

import com.vsbm.receipt.dto.ReceiptDto;
import com.vsbm.receipt.entity.Receipt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {
    Receipt toReceipt(ReceiptDto dto);
    ReceiptDto toReceiptDto(Receipt receipt);
}
