package com.example.care_management_system.repository;

import com.example.care_management_system.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {

    // Optional example methods if you want filtered queries
    List<Pet> findByType(String type);

    List<Pet> findByGender(String gender);

    List<Pet> findByUser_Id(UUID userId);
}
