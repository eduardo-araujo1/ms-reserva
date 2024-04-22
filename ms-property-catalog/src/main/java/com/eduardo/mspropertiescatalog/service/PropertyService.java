package com.eduardo.mspropertiescatalog.service;

import com.eduardo.mspropertiescatalog.converter.PropertyConverter;
import com.eduardo.mspropertiescatalog.dto.PropertyRequestDto;
import com.eduardo.mspropertiescatalog.dto.PropertyResponseDto;
import com.eduardo.mspropertiescatalog.enums.ECity;
import com.eduardo.mspropertiescatalog.model.Property;
import com.eduardo.mspropertiescatalog.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<PropertyResponseDto> findAll(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Property> propertyPage = repository.findAll(pageRequest);
        return propertyPage.map(converter::toDto);
    }

    public Page<PropertyResponseDto> findByCity(ECity city, Pageable pageable){
        Page<Property> propertyPage = repository.findByCity(city, pageable);

        if (propertyPage.isEmpty()){
            throw new RuntimeException("Nenhuma propriedade encontrada nesta cidade.");
        }
        return propertyPage.map(converter::toDto);
    }




}
