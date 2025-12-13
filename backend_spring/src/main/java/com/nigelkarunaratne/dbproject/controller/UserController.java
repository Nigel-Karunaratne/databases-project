package com.nigelkarunaratne.dbproject.controller;

import com.nigelkarunaratne.dbproject.entity.User;
import com.nigelkarunaratne.dbproject.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users") // Base URL for all endpoints in this controller
public class UserController {

    private final UserService userService;

    // Constructor Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --- GET (Read) Operations ---

    // GET /api/users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
            .map(ResponseEntity::ok) // If found, return 200 OK with the User object
            .orElseGet(() -> ResponseEntity.notFound().build()); // If not found, return 404 Not Found
    }

    // --- POST (Create) Operation ---

    // POST /api/users
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Returns 201 Created
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // --- PUT (Update) Operation ---

    // PUT /api/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- DELETE Operation ---

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content (Successful deletion)
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found (User not found)
        }
    }
}