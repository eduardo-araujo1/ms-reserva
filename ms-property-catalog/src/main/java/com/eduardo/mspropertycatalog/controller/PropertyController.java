package com.eduardo.mspropertycatalog.controller;

import com.eduardo.mspropertycatalog.dto.PropertyRequestDto;
import com.eduardo.mspropertycatalog.dto.PropertyResponseDto;
import com.eduardo.mspropertycatalog.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
