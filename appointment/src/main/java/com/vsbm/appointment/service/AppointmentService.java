package com.vsbm.appointment.service;

import com.vsbm.appointment.dto.AppointmentDto;
import com.vsbm.appointment.entity.Appointment;
import com.vsbm.appointment.exception.AppointmentNotFound;
import com.vsbm.appointment.mapper.AppointmentMapper;
import com.vsbm.appointment.repository.AppointmentRepository;
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

    @Transactional
    public void markAsReceiptGenerated(Long appointmentId, Long receiptId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFound("Appointment not found with id: " + appointmentId));
        appointment.setReceiptId(receiptId);
        appointmentRepository.save(appointment);
    }
}
