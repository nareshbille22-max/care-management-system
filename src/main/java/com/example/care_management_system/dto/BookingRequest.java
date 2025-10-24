package com.example.care_management_system.dto;

import com.example.care_management_system.entity.ServiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotBlank(message = "Resource type is required")
    private String resourceType;   // e.g. PET or FAMILY_MEMBER

    @NotNull(message = "Resource ID is required")
    private UUID resourceId;

    @NotNull(message = "Care type is required")
    private ServiceType careType;  // FULL_DAY, HALF_DAY, HOURLY

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    private String notes;

}
