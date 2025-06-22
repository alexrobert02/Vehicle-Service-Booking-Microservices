package com.appointment.mapper;

import com.appointment.dto.AppointmentDto;
import com.appointment.entity.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    Appointment toAppointment(AppointmentDto dto);
    AppointmentDto toAppointmentDto(Appointment vehicle);
}
