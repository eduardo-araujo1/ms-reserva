package com.eduardo.mspropertiescatalog.controller;

import com.eduardo.mspropertiescatalog.dto.PropertyRequestDto;
import com.eduardo.mspropertiescatalog.dto.PropertyResponseDto;
import com.eduardo.mspropertiescatalog.enums.ECity;
import com.eduardo.mspropertiescatalog.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("v1/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService service;

    @PostMapping
    public ResponseEntity<PropertyResponseDto> create(@RequestBody PropertyRequestDto dto){
        var propertyDto = service.registerProperty(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{title}")
                .buildAndExpand(dto.title()).toUri();
        return ResponseEntity.created(uri).body(propertyDto);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<PropertyResponseDto>> listAll(@RequestParam int page, @RequestParam int size){
        Page<PropertyResponseDto> returnAll = service.findAll(page, size);
        return ResponseEntity.ok().body(returnAll);
    }

    @GetMapping("/categories")
    public ResponseEntity<Page<PropertyResponseDto>> filterPropertiesByCity(@RequestParam ECity city, Pageable pageable){
        Page<PropertyResponseDto> responseDtos = service.findByCity(city,pageable);
        return ResponseEntity.ok().body(responseDtos);
    }

}
