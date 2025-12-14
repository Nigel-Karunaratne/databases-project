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
    private final UserService userService; // Inject the UserService to find the User
    private final EntityManager entityManager; // <--- NEW FIELD

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

    //transaction - needs to check if user exists
    // @Transactional
    // public Optional<Manager> createManagerProfile(Integer userId, Manager managerDetails) {
    //     Optional<User> userOptional = userService.findUserById(userId);

    //     if (userOptional.isPresent()) {
    //         // if this user is already a manager don't add
    //         if (managerRepository.existsById(userId)) {
    //              return Optional.empty();
    //         }

    //         User user = userOptional.get();
    //         managerDetails.setUser(user);
            
    //         return Optional.of(managerRepository.save(managerDetails));
    //     }
    //     // no user with given ID
    //     return Optional.empty();
    // }

    @Transactional
    public Optional<Manager> createManagerProfile(Integer userId, Manager managerDetails) {
        System.out.println("  a CREATE MANAGER");
        // 1. Find the associated User entity (REQUIRED for the relationship)
        Optional<User> userOptional = userService.findUserById(userId);
        System.out.println("  b CREATE MANAGER");
        if (userOptional.isEmpty()) {
            System.out.println("  b1 CREATE MANAGER");
            throw new IllegalArgumentException("User ID " + userId + " not found.");
        }
        System.out.println("  c CREATE MANAGER");
        User user = userOptional.get();
        System.out.println("  d CREATE MANAGER");
        if (user.getUserID() == null) {
            System.out.println("  d1 CREATE MANAGER");
            // If the User object itself somehow has a null ID, we can't proceed.
            throw new IllegalStateException("Retrieved User entity has a null ID. Check UserService.");
        }
        System.out.println("  e CREATE MANAGER");
        if (managerRepository.findById(userId).isPresent()) {
            System.out.println("  e1 CREATE MANAGER");
            // Profile already exists
            return Optional.empty(); 
        }
        System.out.println("  f CREATE MANAGER");

        // 2. CRITICAL STEP: Populate the primary key field.
        // A. Set the Foreign Key relationship object (the 'User' object)
        managerDetails.setUser(user); 

        System.out.println("  g CREATE MANAGER");
        
        // B. Explicitly call the setter for the @Id field. 
        // This is necessary because your entity uses @MapsId and Hibernate demands the ID be set.
        managerDetails.setUserID(userId); 

        System.out.println("  h CREATE MANAGER");
        entityManager.persist(managerDetails);
        System.out.println("THE MANAGER DEETS ARE: " + managerDetails.getUserID() + " and user is " + managerDetails.getUser());
        System.out.println("  i CREATE MANAGER");
        // 3. Save the entity
        // return Optional.of(managerRepository.save(managerDetails));
        return Optional.of(managerDetails);
    }

    @Transactional
    public Optional<Manager> updateManagerProfile(Integer userId, Manager managerDetails) {
        
        // 1. Find the existing Manager record by user_id
        return managerRepository.findById(userId).map(existingManager -> {
            
            // 2. Update ONLY the fields specific to the Manager table
            existingManager.setExpertiseArea(managerDetails.getExpertiseArea());
            existingManager.setExperienceYears(managerDetails.getExperienceYears());
            
            // 3. Save the updated entity
            return managerRepository.save(existingManager);
        });
        // If findById(userId) returns Optional.empty(), the map function is skipped
        // and Optional.empty() is returned from the service.
    }

    public boolean deleteManagerProfile(Integer userId) {
        if (managerRepository.existsById(userId)) {
            managerRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}