package com.vsbm.vehicle.mapper;

import com.vsbm.vehicle.dto.VehicleDto;
import com.vsbm.vehicle.entity.Vehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    Vehicle toVehicle(VehicleDto dto);
    VehicleDto toVehicleDto(Vehicle vehicle);
}

