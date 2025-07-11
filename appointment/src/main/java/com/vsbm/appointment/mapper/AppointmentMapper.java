package com.vsbm.appointment.mapper;

import com.vsbm.appointment.dto.AppointmentDto;
import com.vsbm.appointment.entity.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    Appointment toAppointment(AppointmentDto dto);
    AppointmentDto toAppointmentDto(Appointment vehicle);
}
