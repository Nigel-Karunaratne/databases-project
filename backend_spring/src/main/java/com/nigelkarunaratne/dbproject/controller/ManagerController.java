package com.nigelkarunaratne.dbproject.controller;

import com.nigelkarunaratne.dbproject.entity.Manager;
import com.nigelkarunaratne.dbproject.service.ManagerService;

import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/managers")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    // GET /api/managers
    @GetMapping
    public List<Manager> getAllManagers() {
        return managerService.findAllManagers();
    }

    // GET /api/managers/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<Manager> getManagerById(@PathVariable Integer userId) {
        return managerService.findManagerById(userId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/managers/{userId}
    // Use the user's ID in the path to create their profile
    // @PostMapping("/{userId}")
    // public ResponseEntity<Manager> createManagerProfile(@PathVariable Integer userId, @RequestBody Manager managerDetails) {
    //     Optional<Manager> createdManager = managerService.createManagerProfile(userId, managerDetails);
        
    //     if (createdManager.isPresent()) {
    //         // If creation succeeds, return 201 Created
    //         return ResponseEntity.status(HttpStatus.CREATED).body(createdManager.get());
    //     } else {
    //         // If the user doesn't exist or profile already exists, return 404 or 409
    //         return ResponseEntity.badRequest().build(); // Simplified error handling
    //     }
    // }

    @PostMapping // Maps to the base URL: /api/managers
    public ResponseEntity<Manager> createManagerProfile(@RequestBody Map<String, Object> creationRequest) {
        System.out.println("  A INSIDE OF CREATE MANAGER");
        // 1. EXTRACT and CONVERT fields from the JSON Map
        
        String expertiseArea = (String) creationRequest.get("expertiseArea");
        
        // Extract User ID from the JSON body
        Integer userId = null;
        Object userIdObj = creationRequest.get("userID");
        if (userIdObj instanceof Number) {
            userId = ((Number) userIdObj).intValue(); 
        } else if (userIdObj instanceof String) {
            try {
                userId = Integer.parseInt((String) userIdObj);
            } catch (NumberFormatException ignored) {}
        }

        System.out.println("  B INSIDE OF CREATE MANAGER");

        // Extract Experience Years
        Integer experienceYears = null;
        Object experienceYearsObj = creationRequest.get("experienceYears");
        if (experienceYearsObj instanceof Number) {
            experienceYears = ((Number) experienceYearsObj).intValue();
        } else if (experienceYearsObj instanceof String) {
            try {
                experienceYears = Integer.parseInt((String) experienceYearsObj);
            } catch (NumberFormatException ignored) {}
        }
        
        System.out.println("  C INSIDE OF CREATE MANAGER");

        // 2. BASIC VALIDATION
        // All three fields are required to proceed
        if (userId == null || expertiseArea == null || experienceYears == null) {
            // Returns 400 if any required field is missing or incorrectly formatted
            return ResponseEntity.badRequest().body(null); 
        }

        System.out.println("  D INSIDE OF CREATE MANAGER");

        // 3. CONSTRUCT the Manager entity for the service
        Manager newManager = new Manager();
        newManager.setExpertiseArea(expertiseArea);
        newManager.setExperienceYears(experienceYears);

        System.out.println("  E INSIDE OF CREATE MANAGER");
        
        // 4. CALL THE SERVICE (passing the ID extracted from the JSON)
        try {
            Optional<Manager> createdManager = managerService.createManagerProfile(userId, newManager);
            System.out.println("  F INSIDE OF CREATE MANAGER");
            if (createdManager.isPresent()) {
                // Return 201 Created on success
                System.out.println("  F1 INSIDE OF CREATE MANAGER");
                return ResponseEntity.status(HttpStatus.CREATED).body(createdManager.get());
            } else {
                System.out.println("  F2 INSIDE OF CREATE MANAGER");
                // Service returns empty if User ID not found or profile already exists
                return ResponseEntity.badRequest().build(); 
            }
        } catch (Exception e) {
            System.out.println("  FERR INSIDE OF CREATE MANAGER");
            System.err.println("Error creating manager profile: " + e.getMessage());
            return ResponseEntity.internalServerError().build(); 
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Manager> updateManagerProfile(@PathVariable Integer userId, @RequestBody Manager managerDetails) {
        return managerService.updateManagerProfile(userId, managerDetails)
            .map(ResponseEntity::ok) // Returns 200 OK with the updated Manager body
            .orElseGet(() -> ResponseEntity.notFound().build()); // Returns 404 Not Found if the ID doesn't exist
    }

    // DELETE /api/managers/{userId}
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteManagerProfile(@PathVariable Integer userId) {
        if (managerService.deleteManagerProfile(userId)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}