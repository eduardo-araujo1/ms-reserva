package com.eduardo.mspropertiescatalog.service;

import com.eduardo.mspropertiescatalog.converter.PropertyConverter;
import com.eduardo.mspropertiescatalog.dto.PropertyRequestDto;
import com.eduardo.mspropertiescatalog.dto.PropertyResponseDto;
import com.eduardo.mspropertiescatalog.enums.ECity;
import com.eduardo.mspropertiescatalog.model.Property;
import com.eduardo.mspropertiescatalog.repository.PropertyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.eduardo.mspropertiescatalog.enums.ECity.BERTIOGA;
import static com.eduardo.mspropertiescatalog.enums.ECity.UBATUBA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {

    @Mock
    PropertyRepository repository;

    @Mock
    PropertyConverter converter;

    @InjectMocks
    private PropertyService service;

    @Test
    public void insertProperty() {
        PropertyRequestDto dto = new PropertyRequestDto("teste", "teste123", "teste", UBATUBA, 100.50, "http://teste.com");
        Property propertyEntity = new Property(UUID.randomUUID(), dto.address(), dto.pricePerNight(), dto.title(), dto.description(), dto.imageUrl(), dto.city());

        when(converter.toModel(any(PropertyRequestDto.class))).thenReturn(propertyEntity);
        when(repository.save(any(Property.class))).thenReturn(propertyEntity);
        when(converter.toDto(any(Property.class))).thenReturn(new PropertyResponseDto(propertyEntity.getId(), propertyEntity.getTitle(),
                propertyEntity.getAddress(), propertyEntity.getDescription(), propertyEntity.getCity(), propertyEntity.getPricePerNight(),
                propertyEntity.getImageUrl()));

        var insertProperty = service.registerProperty(dto);

        assertNotNull(insertProperty);
        assertEquals(propertyEntity.getId(), insertProperty.id());
        assertEquals(propertyEntity.getPricePerNight(), insertProperty.pricePerNight());
        assertEquals(propertyEntity.getCity(), insertProperty.city());
        verify(repository, times(1)).save(any(Property.class));
    }

    @Test
    public void testFindAll() {
        List<Property> properties = new ArrayList<>();
        properties.add(new Property(UUID.randomUUID(), "teste1", 100.00, "title", "description1", "https://example.com/image.jpg", UBATUBA));
        properties.add(new Property(UUID.randomUUID(), "teste2", 200.00, "title2", "description2", "https://example.com/image2.jpg", BERTIOGA));
        Page<Property> propertiesPage = new PageImpl<>(properties);

        when(repository.findAll(any(PageRequest.class))).thenReturn(propertiesPage);
        when(converter.toDto(any(Property.class))).thenAnswer(invocation -> {
            Property property = invocation.getArgument(0);
            return new PropertyResponseDto(property.getId(), property.getTitle(), property.getAddress(), property.getDescription(), property.getCity(), property.getPricePerNight(), property.getImageUrl());
        });

        Page<PropertyResponseDto> result = service.findAll(0, 10);

        verify(repository, times(1)).findAll(any(PageRequest.class));
        assertThat(result).isNotNull();
    }

    @Test
    public void testFindByCity() {
        ECity city = UBATUBA;
        Pageable pageable = PageRequest.of(0, 10);
        List<Property> properties = new ArrayList<>();
        properties.add(new Property(UUID.randomUUID(), "teste1", 100.00, "title", "description1", "https://example.com/image.jpg", UBATUBA));
        Page<Property> propertyPage = new PageImpl<>(properties);

        when(repository.findByCity(city, pageable)).thenReturn(propertyPage);
        when(converter.toDto(any(Property.class))).thenAnswer(invocation -> {
            Property property = invocation.getArgument(0);
            return new PropertyResponseDto(property.getId(), property.getTitle(), property.getAddress(), property.getDescription(), property.getCity(), property.getPricePerNight(), property.getImageUrl());
        });

        Page<PropertyResponseDto> result = service.findByCity(city, pageable);

        verify(repository, times(1)).findByCity(city, pageable);
        assertFalse(result.isEmpty());
        assertEquals(properties.size(), result.getNumberOfElements());
    }

    @Test
    public void testFindByPrice() {
        Double minPrice = 100.0;
        Double maxPrice = 200.0;
        Pageable pageable = PageRequest.of(0, 10);
        List<Property> properties = new ArrayList<>();
        properties.add(new Property(UUID.randomUUID(), "teste1", 100.00, "title", "description1", "https://example.com/image.jpg", UBATUBA));
        Page<Property> propertyPage = new PageImpl<>(properties);

        when(repository.findBypricePerNightBetween(minPrice, maxPrice, pageable)).thenReturn(propertyPage);
        when(converter.toDto(any(Property.class))).thenAnswer(invocation -> {
            Property property = invocation.getArgument(0);
            return new PropertyResponseDto(property.getId(), property.getTitle(), property.getAddress(), property.getDescription(), property.getCity(), property.getPricePerNight(), property.getImageUrl());
        });

        Page<PropertyResponseDto> result = service.findByPrice(minPrice, maxPrice, pageable);

        verify(repository, times(1)).findBypricePerNightBetween(minPrice, maxPrice, pageable);
        assertFalse(result.isEmpty());
        assertEquals(properties.size(), result.getNumberOfElements());
    }

    @Test
    public void testUpdateProperty() {
        String propertyId = UUID.randomUUID().toString();
        PropertyRequestDto propertyDto = new PropertyRequestDto("teste", "teste123", "teste", UBATUBA, 100.50, "http://teste.com");
        Property existingProperty = new Property(UUID.fromString(propertyId), "teste1", 100.00, "title", "description1", "https://example.com/image.jpg", UBATUBA);
        Property updatedProperty = new Property(UUID.fromString(propertyId), propertyDto.address(), propertyDto.pricePerNight(), propertyDto.title(), propertyDto.description(), propertyDto.imageUrl(), propertyDto.city());
        PropertyResponseDto expectedResponseDto = new PropertyResponseDto(updatedProperty.getId(), updatedProperty.getTitle(), updatedProperty.getAddress(), updatedProperty.getDescription(), updatedProperty.getCity(), updatedProperty.getPricePerNight(), updatedProperty.getImageUrl());

        when(converter.toDto(updatedProperty)).thenReturn(expectedResponseDto);
        when(repository.findById(UUID.fromString(propertyId))).thenReturn(Optional.of(existingProperty));
        when(repository.save(any(Property.class))).thenReturn(updatedProperty);

        PropertyResponseDto updatedPropertyDto = service.update(propertyId, propertyDto);

        verify(repository, times(1)).findById(UUID.fromString(propertyId));
        verify(converter, times(1)).toDto(updatedProperty);
        assertThat(updatedPropertyDto).isEqualTo(expectedResponseDto);
    }

    @Test
    public void testDeleteProperty() {
        String propertyId = UUID.randomUUID().toString();

        when(repository.existsById(UUID.fromString(propertyId))).thenReturn(true);

        service.deleteProperty(propertyId);
        verify(repository).deleteById(UUID.fromString(propertyId));
    }

    @Test
    public void testFindById() {
        String propertyId = UUID.randomUUID().toString();
        Property existingProperty = new Property(UUID.fromString(propertyId), "teste1", 100.00, "title", "description1", "https://example.com/image.jpg", UBATUBA);
        PropertyResponseDto expectedResponseDto = new PropertyResponseDto(existingProperty.getId(), existingProperty.getTitle(), existingProperty.getAddress(), existingProperty.getDescription(), existingProperty.getCity(), existingProperty.getPricePerNight(), existingProperty.getImageUrl());

        when(repository.findById(UUID.fromString(propertyId))).thenReturn(Optional.of(existingProperty));
        when(converter.toDto(existingProperty)).thenReturn(expectedResponseDto);

        PropertyResponseDto foundPropertyDto = service.findById(propertyId);

        assertThat(foundPropertyDto).isEqualTo(expectedResponseDto);
    }
}
