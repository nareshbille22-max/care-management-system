package com.example.care_management_system.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class FamilyMemberRequest {
    private String name;
    private String relation;
    private String gender;
    private LocalDate dateOfBirth;
}
