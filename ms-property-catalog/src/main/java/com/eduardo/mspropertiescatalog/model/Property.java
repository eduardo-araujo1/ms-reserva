package com.eduardo.mspropertiescatalog.model;

import com.eduardo.mspropertiescatalog.enums.ECity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "property_id")
    private UUID propertyId;

    private String address;

    private BigDecimal pricePerNight;

    private String title;

    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ECity city;

}

