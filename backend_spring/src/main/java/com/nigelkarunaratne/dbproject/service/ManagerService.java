package com.nigelkarunaratne.dbproject.service;

import com.nigelkarunaratne.dbproject.entity.Manager;
import com.nigelkarunaratne.dbproject.entity.User;
import com.nigelkarunaratne.dbproject.repository.ManagerRepository;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final UserService userService;
    private final EntityManager entityManager;

    public ManagerService(ManagerRepository managerRepository, UserService userService, EntityManager entityManager) {
        this.managerRepository = managerRepository;
        this.userService = userService;
        this.entityManager = entityManager;
    }

    public List<Manager> findAllManagers() {
        return managerRepository.findAll();
    }

    public Optional<Manager> findManagerById(Integer userId) {
        return managerRepository.findById(userId);
    }

    @Transactional
    public Optional<Manager> createManagerProfile(Integer userId, Manager managerDetails) {
        // Find the associated User entity -- REQUIRED. if cannot find throw err
        Optional<User> userOptional = userService.findUserById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User ID " + userId + " not found.");
        }

        User user = userOptional.get();
        if (user.getUserID() == null) {
            System.out.println("  d1 CREATE MANAGER");
            // If the User object itself somehow has a null ID then abandon ship
            throw new IllegalStateException("Retrieved User entity has a null userID. Check UserService.");
        }
        if (managerRepository.findById(userId).isPresent()) {
            System.out.println("  e1 CREATE MANAGER");
            // Profile already exists
            return Optional.empty(); 
        }

        // Populate primary key field
        // set Foreign Key relationship object (i.e. the User object). No longer also sets id so do that manually
        managerDetails.setUser(user); 
        managerDetails.setUserID(userId); 

        entityManager.persist(managerDetails);
        // Save
        // return Optional.of(managerRepository.save(managerDetails)); // NO LONGER WORKS
        return Optional.of(managerDetails);
    }

    @Transactional
    public Optional<Manager> updateManagerProfile(Integer userId, Manager managerDetails) {
        // Find Manager record by user_id
        return managerRepository.findById(userId).map(existingManager -> {
            // Update ONLY  fields specific to Managers
            existingManager.setExpertiseArea(managerDetails.getExpertiseArea());
            existingManager.setExperienceYears(managerDetails.getExperienceYears());
            return managerRepository.save(existingManager);
        });
        // findById(userId) returns empty, skip map fn and return empty as well
    }

    public boolean deleteManagerProfile(Integer userId) {
        if (managerRepository.existsById(userId)) {
            managerRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}