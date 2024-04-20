package com.eduardo.mspropertycatalog.repository;

import com.eduardo.mspropertycatalog.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID> {
}
