package com.example.care_management_system.dto;

import com.example.care_management_system.entity.FamilyMember;
import com.example.care_management_system.entity.Pet;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResourcesResponse {
    private List<FamilyMember> familyMembers;
    private List<Pet> pets;
}
