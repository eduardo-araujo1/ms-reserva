package com.eduardo.mspropertiescatalog.service;

import com.eduardo.mspropertiescatalog.converter.PropertyConverter;
import com.eduardo.mspropertiescatalog.dto.PropertyRequestDto;
import com.eduardo.mspropertiescatalog.dto.PropertyResponseDto;
import com.eduardo.mspropertiescatalog.enums.ECity;
import com.eduardo.mspropertiescatalog.exception.AddressAlreadyRegisteredException;
import com.eduardo.mspropertiescatalog.exception.PropertyNotFoundException;
import com.eduardo.mspropertiescatalog.model.Property;
import com.eduardo.mspropertiescatalog.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository repository;
    private final PropertyConverter converter;

    public PropertyResponseDto registerProperty(PropertyRequestDto dto) {
        verifyAddressAlreadyRegistered(dto.address());
        Property createProperty = converter.toModel(dto);
        Property savedProperty = repository.save(createProperty);
        return converter.toDto(savedProperty);
    }

    private void verifyAddressAlreadyRegistered(String address) {
        Optional<Property> optionalProperty = repository.findByAddress(address);
        if (optionalProperty.isPresent()) {
            throw new AddressAlreadyRegisteredException("Já existe uma propriedade registrada neste endereço.");
        }
    }

    public Page<PropertyResponseDto> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Property> propertyPage = repository.findAll(pageRequest);
        return propertyPage.map(converter::toDto);
    }

    public PropertyResponseDto findById(String propertyId) {
        Property property = repository.findById(UUID.fromString(propertyId))
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não encontrada ou não existe."));
        return converter.toDto(property);
    }

    public Page<PropertyResponseDto> findByCity(ECity city, Pageable pageable) {
        Page<Property> propertyPage = repository.findByCity(city, pageable);

        if (propertyPage.isEmpty()) {
            throw new PropertyNotFoundException("Nenhuma propriedade encontrada nesta cidade.");
        }
        return propertyPage.map(converter::toDto);
    }

    public Page<PropertyResponseDto> findByPrice(Double minPrice, Double maxPrice, Pageable pageable) {
        Page<Property> propertyPage = repository.findBypricePerNightBetween(minPrice, maxPrice, pageable);

        if (propertyPage.isEmpty()) {
            throw new PropertyNotFoundException("Não encontramos nenhum imóvel neste valor.");
        }
        return propertyPage.map(converter::toDto);
    }

    public PropertyResponseDto update(String propertyId, PropertyRequestDto dto) {
        Property property = repository.findById(UUID.fromString(propertyId))
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não encontrada ou não existe."));

        property.setAddress(dto.address());
        property.setTitle(dto.title());
        property.setDescription(dto.description());
        property.setPricePerNight(dto.pricePerNight());
        property.setCity(dto.city());
        property.setImageUrl(dto.imageUrl());

        Property updatedProperty = repository.save(property);
        return converter.toDto(updatedProperty);
    }

    public void deleteProperty(String propertyId) {
        if (!repository.existsById(UUID.fromString(propertyId))) {
            throw new PropertyNotFoundException("Propriedade não encontrada ou não existe");
        }
        repository.deleteById(UUID.fromString(propertyId));
    }

}
