package com.appointment.service;

import com.appointment.dto.AppointmentDto;
import com.appointment.entity.Appointment;
import com.appointment.exception.AppointmentNotFound;
import com.appointment.mapper.AppointmentMapper;
import com.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    public List<AppointmentDto> findAll() {
        return appointmentRepository.findAll()
                .stream()
                .map(appointmentMapper::toAppointmentDto)
                .toList();
    }

    public AppointmentDto findById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFound("Appointment not found with id: " + id));
        return appointmentMapper.toAppointmentDto(appointment);
    }

    @Transactional
    public AppointmentDto save(AppointmentDto appointmentDto) {
        String clientId = SecurityUtil.getUserId();
        appointmentDto.setClientId(clientId);
        Appointment appointment = appointmentMapper.toAppointment(appointmentDto);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentDto(savedAppointment);
    }
}
