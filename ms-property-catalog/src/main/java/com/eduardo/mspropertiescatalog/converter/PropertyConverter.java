package com.eduardo.mspropertiescatalog.converter;

import com.eduardo.mspropertiescatalog.dto.PropertyRequestDto;
import com.eduardo.mspropertiescatalog.dto.PropertyResponseDto;
import com.eduardo.mspropertiescatalog.model.Property;
import org.springframework.stereotype.Component;

@Component
public class PropertyConverter {

    public Property toModel(PropertyRequestDto dto){
        Property property = new Property();
        property.setTitle(dto.title());
        property.setAddress(dto.address());
        property.setDescription(dto.description());
        property.setCity(dto.city());
        property.setPricePerNight(dto.pricePerNight());
        property.setImageUrl(dto.imageUrl());
        return property;
    }

    public PropertyResponseDto toDto(Property property) {
        return new PropertyResponseDto(
                property.getId(),
                property.getTitle(),
                property.getAddress(),
                property.getDescription(),
                property.getCity(),
                property.getPricePerNight(),
                property.getImageUrl()
        );
    }
}
