package com.nigelkarunaratne.dbproject.controller;

import com.nigelkarunaratne.dbproject.entity.Manager;
import com.nigelkarunaratne.dbproject.service.ManagerService;

import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
    @PostMapping("/{userId}")
    public ResponseEntity<Manager> createManagerProfile(@PathVariable Integer userId, @RequestBody Manager managerDetails) {
        Optional<Manager> createdManager = managerService.createManagerProfile(userId, managerDetails);
        
        if (createdManager.isPresent()) {
            // If creation succeeds, return 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).body(createdManager.get());
        } else {
            // If the user doesn't exist or profile already exists, return 404 or 409
            return ResponseEntity.badRequest().build(); // Simplified error handling
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