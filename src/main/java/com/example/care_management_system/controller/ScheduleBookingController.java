package com.example.care_management_system.controller;

import com.example.care_management_system.dto.ApiResponse;
import com.example.care_management_system.dto.BookingRequest;
import com.example.care_management_system.dto.BookingResponse;
import com.example.care_management_system.entity.ScheduleBooking;
import com.example.care_management_system.service.ScheduleBookingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
public class ScheduleBookingController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleBookingController.class);

    @Autowired
    private ScheduleBookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@Valid @RequestBody BookingRequest request) {
        logger.info("Creating booking for resourceId: {}, type: {}", request.getResourceId(), request.getResourceType());

        BookingResponse booking = bookingService.createBooking(request);

        logger.info("Booking created with ID: {}", booking.getBookingId());
        return ResponseEntity.status(201).body(new ApiResponse<>(booking));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getUserBookings() {
        logger.info("Received request to get user bookings");
        try {
            List<BookingResponse> bookings = bookingService.getBookingsForLoggedInUser();
            logger.info("Returning {} bookings", bookings.size());
            return ResponseEntity.ok(new ApiResponse<>(bookings));
        } catch (Exception e) {
            logger.error("Error fetching bookings: ", e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBooking(@PathVariable UUID id) {
        logger.info("Deleting booking with ID: {}", id);
        bookingService.deleteBooking(id);
        return ResponseEntity.ok(new ApiResponse<>("Booking cancelled successfully"));
    }
}
