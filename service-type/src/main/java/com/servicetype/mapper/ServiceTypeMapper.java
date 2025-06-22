package com.servicetype.mapper;

import com.servicetype.dto.ServiceTypeDto;
import com.servicetype.entity.ServiceType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceTypeMapper {
    ServiceType toServiceType(ServiceTypeDto dto);
    ServiceTypeDto toServiceTypeDto(ServiceType serviceType);
}
