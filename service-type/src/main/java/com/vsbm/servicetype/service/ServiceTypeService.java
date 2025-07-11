package com.vsbm.servicetype.service;

import com.vsbm.servicetype.dto.ServiceTypeDto;
import com.vsbm.servicetype.entity.ServiceType;
import com.vsbm.servicetype.exception.ServiceTypeNotFoundException;
import com.vsbm.servicetype.mapper.ServiceTypeMapper;
import com.vsbm.servicetype.repository.ServiceTypeRepository;
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
            String mechanicId = SecurityUtil.getUserId();
            serviceTypeDto.setMechanicId(mechanicId);
        } else {
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
        serviceTypeRepository.deleteById(id);
    }

    public List<ServiceTypeDto> findByMechanicId(String mechanicId) {
        return serviceTypeRepository.findByMechanicId(mechanicId)
                .stream()
                .map(serviceTypeMapper::toServiceTypeDto)
                .collect(Collectors.toList());
    }
}
