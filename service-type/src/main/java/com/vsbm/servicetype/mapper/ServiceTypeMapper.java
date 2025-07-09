package com.vsbm.servicetype.mapper;

import com.vsbm.servicetype.dto.ServiceTypeDto;
import com.vsbm.servicetype.entity.ServiceType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceTypeMapper {
    ServiceType toServiceType(ServiceTypeDto dto);
    ServiceTypeDto toServiceTypeDto(ServiceType serviceType);
}
