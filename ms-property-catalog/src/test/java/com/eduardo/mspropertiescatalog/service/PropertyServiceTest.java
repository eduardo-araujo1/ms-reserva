package com.eduardo.mspropertiescatalog.service;

import com.eduardo.mspropertiescatalog.converter.PropertyConverter;
import com.eduardo.mspropertiescatalog.dto.PropertyRequestDto;
import com.eduardo.mspropertiescatalog.dto.PropertyResponseDto;
import com.eduardo.mspropertiescatalog.enums.ECity;
import com.eduardo.mspropertiescatalog.exception.AddressAlreadyRegisteredException;
import com.eduardo.mspropertiescatalog.exception.PropertyNotFoundException;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.eduardo.mspropertiescatalog.enums.ECity.BERTIOGA;
import static com.eduardo.mspropertiescatalog.enums.ECity.UBATUBA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    public void testInsertProperty() {
        PropertyRequestDto dto = new PropertyRequestDto("teste", "teste123", "teste", UBATUBA, BigDecimal.valueOf(100.50), "http://teste.com");
        Property propertyEntity = new Property(UUID.randomUUID(), dto.address(), dto.pricePerNight(), dto.title(), dto.description(), dto.imageUrl(), dto.city());

        when(converter.toModel(any(PropertyRequestDto.class))).thenReturn(propertyEntity);
        when(repository.save(any(Property.class))).thenReturn(propertyEntity);
        when(converter.toDto(any(Property.class))).thenReturn(new PropertyResponseDto(propertyEntity.getPropertyId(), propertyEntity.getTitle(),
                propertyEntity.getAddress(), propertyEntity.getDescription(), propertyEntity.getCity(), propertyEntity.getPricePerNight(),
                propertyEntity.getImageUrl()));

        var insertProperty = service.registerProperty(dto);

        assertNotNull(insertProperty);
        assertEquals(propertyEntity.getPropertyId(), insertProperty.propertyId());
        assertEquals(propertyEntity.getPricePerNight(), insertProperty.pricePerNight());
        assertEquals(propertyEntity.getCity(), insertProperty.city());
        verify(repository, times(1)).save(any(Property.class));
    }

    @Test
    public void testInsertProperty_AddressAlreadyRegistered() {
        String NAME = "teste123";
        PropertyRequestDto dto = new PropertyRequestDto("teste", NAME, "teste", UBATUBA, BigDecimal.valueOf(100.50), "http://teste.com"); // Alterado para BigDecimal

        when(repository.findByAddress(NAME)).thenThrow(AddressAlreadyRegisteredException.class);

        assertThatThrownBy(() -> service.registerProperty(dto)).isInstanceOf(AddressAlreadyRegisteredException.class);

        verify(repository, never()).save(any(Property.class));
        verify(repository, times(1)).findByAddress(NAME);
    }


    @Test
    public void testFindAll() {
        List<Property> properties = new ArrayList<>();
        properties.add(new Property(UUID.randomUUID(), "teste1", BigDecimal.valueOf(100.00), "title", "description1", "https://example.com/image.jpg", UBATUBA));
        properties.add(new Property(UUID.randomUUID(), "teste2", BigDecimal.valueOf(200.00), "title2", "description2", "https://example.com/image2.jpg", BERTIOGA));
        Page<Property> propertiesPage = new PageImpl<>(properties);

        when(repository.findAll(any(PageRequest.class))).thenReturn(propertiesPage);
        when(converter.toDto(any(Property.class))).thenAnswer(invocation -> {
            Property property = invocation.getArgument(0);
            return new PropertyResponseDto(property.getPropertyId(), property.getTitle(), property.getAddress(), property.getDescription(), property.getCity(), property.getPricePerNight(), property.getImageUrl());
        });

        Page<PropertyResponseDto> result = service.findAll(0, 10);

        verify(repository, times(1)).findAll(any(PageRequest.class));
        assertThat(result).isNotNull();
    }

    @Test
    public void testFindById() {
        String propertyId = UUID.randomUUID().toString();
        Property existingProperty = new Property(UUID.fromString(propertyId), "teste1", BigDecimal.valueOf(100.00), "title", "description1", "https://example.com/image.jpg", UBATUBA);
        PropertyResponseDto expectedResponseDto = new PropertyResponseDto(existingProperty.getPropertyId(), existingProperty.getTitle(), existingProperty.getAddress(), existingProperty.getDescription(), existingProperty.getCity(), existingProperty.getPricePerNight(), existingProperty.getImageUrl());

        when(repository.findById(UUID.fromString(propertyId))).thenReturn(Optional.of(existingProperty));
        when(converter.toDto(existingProperty)).thenReturn(expectedResponseDto);

        PropertyResponseDto foundPropertyDto = service.findById(propertyId);

        assertThat(foundPropertyDto).isEqualTo(expectedResponseDto);
    }



    @Test
    public void testFindById_PropertyNotFound() {
        String nonExistentPropertyId = UUID.randomUUID().toString();

        when(repository.findById(UUID.fromString(nonExistentPropertyId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(nonExistentPropertyId))
                .isInstanceOf(PropertyNotFoundException.class)
                .hasMessage("Propriedade não encontrada ou não existe.");
    }

    @Test
    public void testFindByCity() {
        ECity city = UBATUBA;
        Pageable pageable = PageRequest.of(0, 10);
        List<Property> properties = new ArrayList<>();
        properties.add(new Property(UUID.randomUUID(), "teste1", BigDecimal.valueOf(100.00), "title", "description1", "https://example.com/image.jpg", UBATUBA));
        Page<Property> propertyPage = new PageImpl<>(properties);

        when(repository.findByCity(city, pageable)).thenReturn(propertyPage);
        when(converter.toDto(any(Property.class))).thenAnswer(invocation -> {
            Property property = invocation.getArgument(0);
            return new PropertyResponseDto(property.getPropertyId(), property.getTitle(), property.getAddress(), property.getDescription(), property.getCity(), property.getPricePerNight(), property.getImageUrl());
        });

        Page<PropertyResponseDto> result = service.findByCity(city, pageable);

        verify(repository, times(1)).findByCity(city, pageable);
        assertFalse(result.isEmpty());
        assertEquals(properties.size(), result.getNumberOfElements());
    }

    @Test
    public void testFindByCity_NoPropertiesFound() {
        ECity city = UBATUBA;
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findByCity(city, pageable)).thenReturn(Page.empty());

        assertThatThrownBy(() -> service.findByCity(city, pageable))
                .isInstanceOf(PropertyNotFoundException.class)
                .hasMessage("Nenhuma propriedade encontrada nesta cidade.");
    }

    @Test
    public void testFindByPrice() {
        BigDecimal minPrice = BigDecimal.valueOf(100.0);
        BigDecimal maxPrice = BigDecimal.valueOf(200.0);
        Pageable pageable = PageRequest.of(0, 10);
        List<Property> properties = new ArrayList<>();
        properties.add(new Property(UUID.randomUUID(), "teste1", BigDecimal.valueOf(100.00), "title", "description1", "https://example.com/image.jpg", UBATUBA));
        Page<Property> propertyPage = new PageImpl<>(properties);

        when(repository.findBypricePerNightBetween(minPrice, maxPrice, pageable)).thenReturn(propertyPage);
        when(converter.toDto(any(Property.class))).thenAnswer(invocation -> {
            Property property = invocation.getArgument(0);
            return new PropertyResponseDto(property.getPropertyId(), property.getTitle(), property.getAddress(), property.getDescription(), property.getCity(), property.getPricePerNight(), property.getImageUrl());
        });

        Page<PropertyResponseDto> result = service.findByPrice(minPrice, maxPrice, pageable);

        verify(repository, times(1)).findBypricePerNightBetween(minPrice, maxPrice, pageable);
        assertFalse(result.isEmpty());
        assertEquals(properties.size(), result.getNumberOfElements());
    }

    @Test
    public void testFindByPrice_NoPropertiesFound() {
        BigDecimal minPrice = BigDecimal.valueOf(100.0);
        BigDecimal maxPrice = BigDecimal.valueOf(200.0);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findBypricePerNightBetween(minPrice, maxPrice, pageable)).thenReturn(Page.empty());

        assertThatThrownBy(() -> service.findByPrice(minPrice, maxPrice, pageable))
                .isInstanceOf(PropertyNotFoundException.class)
                .hasMessage("Não encontramos nenhum imóvel neste valor.");
    }

    @Test
    public void testUpdateProperty() {
        String propertyId = UUID.randomUUID().toString();
        PropertyRequestDto propertyDto = new PropertyRequestDto("teste", "teste123", "teste", UBATUBA, BigDecimal.valueOf(100.50), "http://teste.com");
        Property existingProperty = new Property(UUID.fromString(propertyId), "teste1", BigDecimal.valueOf(100.00), "title", "description1", "https://example.com/image.jpg", UBATUBA);
        Property updatedProperty = new Property(UUID.fromString(propertyId), propertyDto.address(), propertyDto.pricePerNight(), propertyDto.title(), propertyDto.description(), propertyDto.imageUrl(), propertyDto.city());
        PropertyResponseDto expectedResponseDto = new PropertyResponseDto(updatedProperty.getPropertyId(), updatedProperty.getTitle(), updatedProperty.getAddress(), updatedProperty.getDescription(), updatedProperty.getCity(), updatedProperty.getPricePerNight(), updatedProperty.getImageUrl());

        when(converter.toDto(updatedProperty)).thenReturn(expectedResponseDto);
        when(repository.findById(UUID.fromString(propertyId))).thenReturn(Optional.of(existingProperty));
        when(repository.save(any(Property.class))).thenReturn(updatedProperty);

        PropertyResponseDto updatedPropertyDto = service.update(propertyId, propertyDto);

        verify(repository, times(1)).findById(UUID.fromString(propertyId));
        verify(converter, times(1)).toDto(updatedProperty);
        assertThat(updatedPropertyDto).isEqualTo(expectedResponseDto);
    }


    @Test
    public void testUpdateProperty_PropertyNotFound() {
        String nonExistingPropertyId = UUID.randomUUID().toString();
        PropertyRequestDto propertyDto = new PropertyRequestDto("teste", "teste123", "teste", UBATUBA, BigDecimal.valueOf(100.50), "http://teste.com");

        when(repository.findById(UUID.fromString(nonExistingPropertyId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(nonExistingPropertyId, propertyDto))
                .isInstanceOf(PropertyNotFoundException.class)
                .hasMessage("Propriedade não encontrada ou não existe.");

        verify(repository, times(1)).findById(UUID.fromString(nonExistingPropertyId));
    }

    @Test
    public void testDeleteProperty() {
        String propertyId = UUID.randomUUID().toString();

        when(repository.existsById(UUID.fromString(propertyId))).thenReturn(true);

        service.deleteProperty(propertyId);
        verify(repository).deleteById(UUID.fromString(propertyId));
    }

    @Test
    public void testDeleteProperty_PropertyNotFound() {
        String nonExistingPropertyId = UUID.randomUUID().toString();

        when(repository.existsById(UUID.fromString(nonExistingPropertyId))).thenReturn(false);

        assertThatThrownBy(() -> service.deleteProperty(nonExistingPropertyId))
                .isInstanceOf(PropertyNotFoundException.class)
                .hasMessage("Propriedade não encontrada ou não existe");

        verify(repository, times(1)).existsById(UUID.fromString(nonExistingPropertyId));
        verify(repository, never()).deleteById(any(UUID.class));
    }

}
