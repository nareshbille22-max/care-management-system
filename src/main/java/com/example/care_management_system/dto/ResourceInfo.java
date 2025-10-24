package com.example.care_management_system.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class ResourceInfo {
    private UUID resourceId;
    private String name;

    public ResourceInfo(UUID resourceId, String name) {
        this.resourceId = resourceId;
        this.name = name;
    }

}
