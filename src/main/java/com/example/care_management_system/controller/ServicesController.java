package com.example.care_management_system.controller;

import com.example.care_management_system.dto.ApiResponse;
import com.example.care_management_system.entity.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServicesController {

    private static final Logger logger = LoggerFactory.getLogger(ServicesController.class);

    @GetMapping("/care-types")
    public ResponseEntity<ApiResponse<List<ServiceType>>> getCareTypes() {
        logger.info("Fetching care types");
        List<ServiceType> careTypes = Arrays.asList(ServiceType.values());
        logger.debug("Returned care types: {}", careTypes);
        return ResponseEntity.ok(new ApiResponse<>(careTypes));
    }
}
