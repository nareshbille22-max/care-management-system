package com.example.care_management_system.service;

import com.example.care_management_system.dto.PetRequest;
import com.example.care_management_system.entity.Pet;
import com.example.care_management_system.entity.User;
import com.example.care_management_system.repository.PetRepository;
import com.example.care_management_system.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AuthService authService;

    public Pet addPet(PetRequest request) {
        logger.info("Adding new pet: {}", request.getName());
        String email = authService.getLoggedInUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        Pet pet = new Pet();
        pet.setName(request.getName());
        pet.setType(request.getType());
        pet.setBreed(request.getBreed());
        pet.setGender(request.getGender());
        pet.setAge(request.getAge());
        pet.setUser(user);

        Pet savedPet = petRepository.save(pet);
        logger.info("Pet added with ID: {}", savedPet.getId());
        return savedPet;
    }

    public List<Pet> getPetsForLoggedInUser() {
        String email = authService.getLoggedInUsername();
        logger.info("Fetching pets for user: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return user.getPets();
    }

    public Pet updatePet(UUID petId, PetRequest request) {
        logger.info("Updating pet with ID: {}", petId);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + petId));

        pet.setName(request.getName());
        pet.setType(request.getType());
        pet.setBreed(request.getBreed());
        pet.setGender(request.getGender());
        pet.setAge(request.getAge());

        Pet updatedPet = petRepository.save(pet);
        logger.info("Pet updated with ID: {}", updatedPet.getId());
        return updatedPet;
    }

    public void deletePet(UUID petId) {
        logger.info("Deleting pet with ID: {}", petId);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + petId));
        petRepository.delete(pet);
        logger.info("Pet deleted with ID: {}", petId);
    }
}
