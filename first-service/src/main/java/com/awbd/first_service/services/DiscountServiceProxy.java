package com.awbd.first_service.services;

import com.awbd.first_service.model.Discount;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "discount", url = "localhost:8081")
public interface DiscountServiceProxy {
    @GetMapping("/discount")
    Discount findDiscount();
}
