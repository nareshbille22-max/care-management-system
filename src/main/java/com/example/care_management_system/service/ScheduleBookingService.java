package com.example.care_management_system.service;

import com.example.care_management_system.dto.BookingRequest;
import com.example.care_management_system.dto.BookingResponse;
import com.example.care_management_system.dto.ResourceInfo;
import com.example.care_management_system.entity.*;
import com.example.care_management_system.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ScheduleBookingService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleBookingService.class);

    @Autowired
    private ScheduleBookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    private AuthService authService;

    private void validateResource(String resourceType, UUID resourceId) {
        if ("PET".equalsIgnoreCase(resourceType)) {
            if (!petRepository.existsById(resourceId)) {
                throw new IllegalArgumentException("Pet not found with id: " + resourceId);
            }
        } else if ("FAMILY_MEMBER".equalsIgnoreCase(resourceType)) {
            if (!familyMemberRepository.existsById(resourceId)) {
                throw new IllegalArgumentException("Family member not found with id: " + resourceId);
            }
        } else {
            throw new IllegalArgumentException("Invalid resource type: " + resourceType);
        }
    }

    public BookingResponse createBooking(BookingRequest request) {
        logger.info("Creating booking for resource: {}", request.getResourceId());
        
        String email = authService.getLoggedInUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

        ScheduleBooking booking = new ScheduleBooking();
        booking.setResourceType(request.getResourceType());
        booking.setResourceId(request.getResourceId());
        booking.setCareType(request.getCareType());
        booking.setAppointmentDate(request.getAppointmentDate());
        booking.setNotes(request.getNotes());
        booking.setPaymentStatus("PENDING");
        booking.setUser(user);

        validateResource(request.getResourceType(), request.getResourceId());

        ScheduleBooking savedBooking = bookingRepository.save(booking);
        logger.info("Booking created with ID: {}", savedBooking.getBookingId());

        ResourceInfo resourceInfo = getResourceInfo(savedBooking.getResourceType(), savedBooking.getResourceId());

        return new BookingResponse(
            savedBooking.getBookingId(),
            savedBooking.getResourceType(),
            resourceInfo,
            savedBooking.getCareType().name(),
            savedBooking.getAppointmentDate(),
            savedBooking.getNotes(),
            savedBooking.getPaymentStatus(),
            savedBooking.getCreatedAt(),
            savedBooking.getUpdatedAt()
        );
    }

    public List<BookingResponse> getBookingsForLoggedInUser() {
        logger.info("Fetching bookings for logged-in user");
        
        String email = authService.getLoggedInUsername();
        logger.debug("User email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));
        
        logger.debug("User ID: {}", user.getId());
        
        List<ScheduleBooking> bookings = bookingRepository.findAllByUser(user);
        logger.info("Found {} bookings for user", bookings.size());
        
        return bookings.stream()
                .map(booking -> {
                    logger.debug("Processing booking ID: {}", booking.getBookingId());
                    ResourceInfo resourceInfo = getResourceInfo(booking.getResourceType(), booking.getResourceId());
                    return new BookingResponse(
                        booking.getBookingId(),
                        booking.getResourceType(),
                        resourceInfo,
                        booking.getCareType().name(),
                        booking.getAppointmentDate(),
                        booking.getNotes(),
                        booking.getPaymentStatus(),
                        booking.getCreatedAt(),
                        booking.getUpdatedAt()
                    );
                })
                .collect(Collectors.toList());
    }

    private ResourceInfo getResourceInfo(String resourceType, UUID resourceId) {
        logger.debug("Fetching resource info for type: {} and ID: {}", resourceType, resourceId);
        
        if ("PET".equalsIgnoreCase(resourceType)) {
            Pet pet = petRepository.findById(resourceId)
                    .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + resourceId));
            return new ResourceInfo(pet.getId(), pet.getName());
        } else if ("FAMILY_MEMBER".equalsIgnoreCase(resourceType)) {
            FamilyMember member = familyMemberRepository.findById(resourceId)
                    .orElseThrow(() -> new EntityNotFoundException("Family member not found with id: " + resourceId));
            return new ResourceInfo(member.getId(), member.getName());
        }
        throw new IllegalArgumentException("Invalid resource type: " + resourceType);
    }

    public void deleteBooking(UUID bookingId) {
        logger.info("Deleting booking with ID: {}", bookingId);
        ScheduleBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));
        bookingRepository.delete(booking);
        logger.info("Booking deleted successfully");
    }
}
