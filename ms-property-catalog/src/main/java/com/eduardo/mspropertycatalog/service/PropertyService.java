package com.eduardo.mspropertycatalog.service;

import com.eduardo.mspropertycatalog.converter.PropertyConverter;
import com.eduardo.mspropertycatalog.dto.PropertyRequestDto;
import com.eduardo.mspropertycatalog.dto.PropertyResponseDto;
import com.eduardo.mspropertycatalog.model.Property;
import com.eduardo.mspropertycatalog.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository repository;
    private final PropertyConverter converter;

    public PropertyResponseDto registerProperty(PropertyRequestDto dto){
        Property createProperty = converter.toModel(dto);
        Property savedProperty = repository.save(createProperty);
        return converter.toDto(savedProperty);
    }


}
