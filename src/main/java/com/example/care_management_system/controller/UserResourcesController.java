package com.example.care_management_system.controller;

import com.example.care_management_system.dto.ApiResponse;
import com.example.care_management_system.dto.UserResourcesResponse;
import com.example.care_management_system.entity.FamilyMember;
import com.example.care_management_system.entity.Pet;
import com.example.care_management_system.service.FamilyMemberService;
import com.example.care_management_system.service.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserResourcesController {

    private static final Logger logger = LoggerFactory.getLogger(UserResourcesController.class);

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private PetService petService;

    @GetMapping("/resources")
    public ResponseEntity<ApiResponse<UserResourcesResponse>> getUserResources() {
        logger.info("Received request to get family members and pets for logged-in user");

        List<FamilyMember> familyMembers = familyMemberService.getFamilyMembersForLoggedInUser();
        List<Pet> pets = petService.getPetsForLoggedInUser();

        logger.debug("Found {} family members and {} pets", familyMembers.size(), pets.size());

        UserResourcesResponse response = new UserResourcesResponse(familyMembers, pets);

        return ResponseEntity.ok(new ApiResponse<>(response));
    }
}
