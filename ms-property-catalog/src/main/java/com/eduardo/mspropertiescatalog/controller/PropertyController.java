package com.eduardo.mspropertiescatalog.controller;

import com.eduardo.mspropertiescatalog.dto.PropertyRequestDto;
import com.eduardo.mspropertiescatalog.dto.PropertyResponseDto;
import com.eduardo.mspropertiescatalog.enums.ECity;
import com.eduardo.mspropertiescatalog.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Page<PropertyResponseDto>> listAll(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size){
        Page<PropertyResponseDto> returnAll = service.findAll(page, size);
        return ResponseEntity.ok().body(returnAll);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponseDto> findById(@PathVariable String id){
        PropertyResponseDto findById = service.findById(id);
        return ResponseEntity.ok().body(findById);
    }

    @GetMapping("/categories")
    public ResponseEntity<Page<PropertyResponseDto>> filterPropertiesByCity(@RequestParam ECity city,
                                                                            @PageableDefault(page = 0, size = 10) Pageable pageable){
        Page<PropertyResponseDto> responseDtos = service.findByCity(city,pageable);
        return ResponseEntity.ok().body(responseDtos);
    }

    @GetMapping("/price")
    public ResponseEntity<Page<PropertyResponseDto>> filterPropertiesByPrice(@RequestParam Double minPrice,
                                                                             @RequestParam Double maxPrice,
                                                                             @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<PropertyResponseDto> responseDtos = service.findByPrice(minPrice, maxPrice, pageable);
        return ResponseEntity.ok().body(responseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyResponseDto> updateProperty(@PathVariable String id,
                                                              @RequestBody PropertyRequestDto dto){
        PropertyResponseDto update = service.update(id, dto);
        return ResponseEntity.ok().body(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PropertyResponseDto> deleteProperty(@PathVariable String id){
        service.deleteProperty(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
