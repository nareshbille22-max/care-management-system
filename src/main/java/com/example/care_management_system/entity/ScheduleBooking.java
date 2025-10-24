package com.example.care_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "schedule_bookings")
@EntityListeners(AuditingEntityListener.class)
public class ScheduleBooking {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID bookingId;

    @Column(nullable = false)
    private String resourceType;     // "PET" or "FAMILY_MEMBER"

    @Column(nullable = false)
    private UUID resourceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType careType;    // FULL_DAY, HALF_DAY, HOURLY

    @Column(nullable = false)
    private LocalDate appointmentDate;

    private String notes;

    private String paymentStatus;    // e.g., PENDING, PAID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
