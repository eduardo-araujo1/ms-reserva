package com.eduardo.mspropertiescatalog.model;

import com.eduardo.mspropertiescatalog.enums.ECity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private UUID id;

    private String address;

    private Double pricePerNight;

    private String title;

    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ECity city;

}

