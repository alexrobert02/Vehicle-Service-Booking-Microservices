package com.vehicle.mapper;

import com.vehicle.dto.VehicleDto;
import com.vehicle.entity.Vehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    Vehicle toVehicle(VehicleDto dto);
    VehicleDto toVehicleDto(Vehicle vehicle);
}

