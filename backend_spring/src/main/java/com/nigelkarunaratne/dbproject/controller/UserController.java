package com.nigelkarunaratne.dbproject.controller;

import com.nigelkarunaratne.dbproject.entity.User;
import com.nigelkarunaratne.dbproject.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/users") // Base URL for all endpoints for users
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users (OPERATION: Read)
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    // GET /api/users/{id} (OPERATION: Read)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.findUserById(id)
            .map(ResponseEntity::ok) // If found, return 200 OK with the User object
            .orElseGet(() -> ResponseEntity.notFound().build()); // If not found, return 404 Not Found
    }

    // POST /api/users (OPERATION: Create)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Returns 201 Created
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // PUT /api/users/{id} (OPERATION: Update)
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/users/{id} (OPERATION: Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content (deletion successful)
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found (User not found)
        }
    }
}