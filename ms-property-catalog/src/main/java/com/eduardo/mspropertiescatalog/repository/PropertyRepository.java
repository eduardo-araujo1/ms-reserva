package com.eduardo.mspropertiescatalog.repository;

import com.eduardo.mspropertiescatalog.enums.ECity;
import com.eduardo.mspropertiescatalog.model.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID> {

    Optional<Property> findByAddress(String address);
    Page<Property> findByCity(ECity city, Pageable pageable);

    Page<Property> findBypricePerNightBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
