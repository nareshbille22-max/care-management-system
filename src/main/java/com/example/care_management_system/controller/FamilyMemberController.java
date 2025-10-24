package com.example.care_management_system.controller;

import com.example.care_management_system.dto.ApiResponse;
import com.example.care_management_system.dto.FamilyMemberRequest;
import com.example.care_management_system.entity.FamilyMember;
import com.example.care_management_system.service.FamilyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/family")
public class FamilyMemberController {

    @Autowired
    private FamilyMemberService familyMemberService;

    @PostMapping
    public ResponseEntity<ApiResponse<FamilyMember>> addFamilyMember(@RequestBody FamilyMemberRequest request) {
        FamilyMember familyMember = familyMemberService.addFamilyMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(familyMember));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FamilyMember>> updateFamilyMember(@PathVariable UUID id, @RequestBody FamilyMemberRequest request) {
        FamilyMember familyMember = familyMemberService.updateFamilyMember(id, request);
        return ResponseEntity.ok(new ApiResponse<>(familyMember));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamilyMember(@PathVariable UUID id) {
        familyMemberService.deleteFamilyMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FamilyMember>>> getAllFamilyMembers() {
        List<FamilyMember> familyMembers = familyMemberService.getFamilyMembersForLoggedInUser();
        ApiResponse<List<FamilyMember>> response = new ApiResponse<>(familyMembers);
        return ResponseEntity.ok(response);
    }

}
