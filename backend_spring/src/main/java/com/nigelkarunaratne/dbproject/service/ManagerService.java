package com.nigelkarunaratne.dbproject.service;

import com.nigelkarunaratne.dbproject.entity.Manager;
import com.nigelkarunaratne.dbproject.entity.User;
import com.nigelkarunaratne.dbproject.repository.ManagerRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final UserService userService; // Inject the UserService to find the User

    public ManagerService(ManagerRepository managerRepository, UserService userService) {
        this.managerRepository = managerRepository;
        this.userService = userService;
    }

    public List<Manager> findAllManagers() {
        return managerRepository.findAll();
    }

    public Optional<Manager> findManagerById(Integer userId) {
        return managerRepository.findById(userId);
    }

    //transaction - needs to check if user exists
    @Transactional
    public Optional<Manager> createManagerProfile(Integer userId, Manager managerDetails) {
        Optional<User> userOptional = userService.findUserById(userId);

        if (userOptional.isPresent()) {
            // if this user is already a manager don't add
            if (managerRepository.existsById(userId)) {
                 return Optional.empty();
            }

            User user = userOptional.get();
            managerDetails.setUser(user);
            
            return Optional.of(managerRepository.save(managerDetails));
        }
        // no user with given ID
        return Optional.empty();
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