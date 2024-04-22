package com.eduardo.mspropertiescatalog.repository;

import com.eduardo.mspropertiescatalog.enums.ECity;
import com.eduardo.mspropertiescatalog.model.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID> {

    Page<Property> findByCity(ECity city, Pageable pageable);

    Page<Property> findBypricePerNightBetween(Double minPrice, Double maxPrice, Pageable pageable);
}
