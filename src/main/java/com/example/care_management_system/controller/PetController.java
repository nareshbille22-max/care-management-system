package com.example.care_management_system.controller;

import com.example.care_management_system.dto.ApiResponse;
import com.example.care_management_system.dto.PetRequest;
import com.example.care_management_system.entity.Pet;
import com.example.care_management_system.service.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<ApiResponse<Pet>> addPet(@RequestBody PetRequest request) {
        logger.info("Received request to add pet");
        Pet pet = petService.addPet(request);
        return ResponseEntity.status(201).body(new ApiResponse<>(pet));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Pet>>> getPets() {
        logger.info("Received request to get pets");
        List<Pet> pets = petService.getPetsForLoggedInUser();
        return ResponseEntity.ok(new ApiResponse<>(pets));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Pet>> updatePet(@PathVariable UUID id, @RequestBody PetRequest request) {
        logger.info("Received request to update pet with id {}", id);
        Pet pet = petService.updatePet(id, request);
        return ResponseEntity.ok(new ApiResponse<>(pet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable UUID id) {
        logger.info("Received request to delete pet with id {}", id);
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}
