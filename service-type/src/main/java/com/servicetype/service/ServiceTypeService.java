package com.servicetype.service;

import com.servicetype.dto.ServiceTypeDto;
import com.servicetype.entity.ServiceType;
import com.servicetype.exception.ServiceTypeNotFoundException;
import com.servicetype.mapper.ServiceTypeMapper;
import com.servicetype.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceTypeMapper serviceTypeMapper;

    public List<ServiceTypeDto> findAll() {
        return serviceTypeRepository.findAll()
                .stream()
                .map(serviceTypeMapper::toServiceTypeDto)
                .collect(Collectors.toList());
    }

    public ServiceTypeDto findById(Long id) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new ServiceTypeNotFoundException("ServiceType not found with id: " + id));
        return serviceTypeMapper.toServiceTypeDto(serviceType);
    }

    @Transactional
    public ServiceTypeDto save(ServiceTypeDto serviceTypeDto) {
        if (serviceTypeDto.getId() == null) {
            // Creare nou serviciu -> setăm mechanicId din utilizatorul curent
            String mechanicId = SecurityUtil.getUserId();
            serviceTypeDto.setMechanicId(mechanicId);
        } else {
            // Update -> păstrăm mechanicId-ul existent din baza de date
            ServiceType existing = serviceTypeRepository.findById(serviceTypeDto.getId())
                    .orElseThrow(() -> new ServiceTypeNotFoundException("ServiceType not found with id: " + serviceTypeDto.getId()));
            serviceTypeDto.setMechanicId(existing.getMechanicId());
        }

        ServiceType serviceType = serviceTypeMapper.toServiceType(serviceTypeDto);
        ServiceType savedServiceType = serviceTypeRepository.save(serviceType);
        return serviceTypeMapper.toServiceTypeDto(savedServiceType);
    }


    @Transactional
    public void deleteById(Long id) {
        if (!serviceTypeRepository.existsById(id)) {
            throw new ServiceTypeNotFoundException("ServiceType not found with id: " + id);
        }
        // In a microservice architecture, complex cascading deletes or checks
        // against other services (like appointments) should be handled carefully.
        // This might involve event-driven communication or direct API calls.
        // For now, we'll keep it simple and just delete the service type.
        serviceTypeRepository.deleteById(id);
    }

    public List<ServiceTypeDto> findByMechanicId(String mechanicId) {
        return serviceTypeRepository.findByMechanicId(mechanicId)
                .stream()
                .map(serviceTypeMapper::toServiceTypeDto)
                .collect(Collectors.toList());
    }
}
