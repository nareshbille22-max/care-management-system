package com.example.care_management_system.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PetRequest {
    private String name;
    private String type;
    private String breed;
    private String gender;
    private Integer age;
}
