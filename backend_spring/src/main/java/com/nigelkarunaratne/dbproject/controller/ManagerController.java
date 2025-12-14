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

    // GET
    // /api/managers
    @GetMapping
    public List<Manager> getAllManagers() {
        return managerService.findAllManagers();
    }

    // GET
    // /api/managers/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<Manager> getManagerById(@PathVariable Integer userId) {
        return managerService.findManagerById(userId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/managers (create)
    @PostMapping
    public ResponseEntity<Manager> createManagerProfile(@RequestBody Map<String, Object> creationRequest) {
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
        // Extract ExperienceYears
        Integer experienceYears = null;
        Object experienceYearsObj = creationRequest.get("experienceYears");
        if (experienceYearsObj instanceof Number) {
            experienceYears = ((Number) experienceYearsObj).intValue();
        } else if (experienceYearsObj instanceof String) {
            try {
                experienceYears = Integer.parseInt((String) experienceYearsObj);
            } catch (NumberFormatException ignored) {}
        }
        // we need all 3
        if (userId == null || expertiseArea == null || experienceYears == null) {
            // Returns 400
            return ResponseEntity.badRequest().body(null); 
        }
        Manager newManager = new Manager();
        newManager.setExpertiseArea(expertiseArea);
        newManager.setExperienceYears(experienceYears);
        
        // SERVICE CALLS
        try {
            Optional<Manager> createdManager = managerService.createManagerProfile(userId, newManager);
            if (createdManager.isPresent()) {
                // Return 201 Created on success
                return ResponseEntity.status(HttpStatus.CREATED).body(createdManager.get());
            } else {
                // return empty if User ID not found OR profile already exists
                return ResponseEntity.badRequest().build(); 
            }
        } catch (Exception e) {
            System.err.println("Error creating manager profile: " + e.getMessage());
            return ResponseEntity.internalServerError().build(); 
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Manager> updateManagerProfile(@PathVariable Integer userId, @RequestBody Manager managerDetails) {
        return managerService.updateManagerProfile(userId, managerDetails)
            .map(ResponseEntity::ok) // Returns 200 OK with updated Manager body
            .orElseGet(() -> ResponseEntity.notFound().build()); // Returns 404 if ID doesn't exist
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