package com.example.care_management_system.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class BookingResponse {
    private UUID bookingId;
    private String resourceType;
    private ResourceInfo resource;
    private String careType;
    private LocalDate appointmentDate;
    private String notes;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public BookingResponse(UUID bookingId, String resourceType, ResourceInfo resource, 
                          String careType, LocalDate appointmentDate, 
                          String notes, String paymentStatus,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.bookingId = bookingId;
        this.resourceType = resourceType;
        this.resource = resource;
        this.careType = careType;
        this.appointmentDate = appointmentDate;
        this.notes = notes;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
