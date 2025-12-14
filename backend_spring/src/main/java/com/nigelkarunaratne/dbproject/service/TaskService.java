package com.nigelkarunaratne.dbproject.service;

import com.nigelkarunaratne.dbproject.entity.Task;
import com.nigelkarunaratne.dbproject.entity.Project;
import com.nigelkarunaratne.dbproject.entity.User;
import com.nigelkarunaratne.dbproject.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    // Assuming Project and User services exist and use Integer for findById
    private final ProjectService projectService; 
    private final UserService userService; 

    public TaskService(TaskRepository taskRepository, ProjectService projectService, UserService userService) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
        this.userService = userService;
    }

    // --- READ OPERATIONS ---

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> findTaskById(Integer id) {
        return taskRepository.findById(id);
    }

    public List<Task> findTasksByUserId(Integer userId) {
        // Use the custom finder method from the repository
        return taskRepository.findByUserUserID(userId);
    }

    // --- CREATE OPERATION ---

    @Transactional
    public Optional<Task> createTask(Task taskDetails, Integer newProjectID, Integer newAssignedUserID) {
        
        // 1. Validate Project (Required/NOT NULL)
        Optional<Project> projectOptional = projectService.findProjectById(newProjectID);
        if (projectOptional.isEmpty()) {
            throw new IllegalArgumentException("Project ID is invalid.");
        }
        taskDetails.setProject(projectOptional.get());

        // 2. Validate User (Required/NOT NULL)
        Optional<User> userOptional = userService.findUserById(newAssignedUserID);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User ID is invalid.");
        }
        taskDetails.setUser(userOptional.get());
        
        // 3. Save
        return Optional.of(taskRepository.save(taskDetails));
    }

    // --- UPDATE OPERATION ---

    @Transactional
    public Optional<Task> updateTask(Integer taskId, Task taskDetails, Integer newProjectID, Integer newAssignedUserID) {
        
        return taskRepository.findById(taskId).map(existingTask -> {
            
            // 1. Update Core Attributes
            existingTask.setTitle(taskDetails.getTitle());
            existingTask.setDescription(taskDetails.getDescription());
            existingTask.setPriority(taskDetails.getPriority());
            existingTask.setDueDate(taskDetails.getDueDate());

            // 2. Validate and Update Assigned Project
            Optional<Project> projectOptional = projectService.findProjectById(newProjectID);
            if (projectOptional.isEmpty()) {
                throw new IllegalArgumentException("Invalid Project ID provided for update.");
            }
            existingTask.setProject(projectOptional.get());

            // 3. Validate and Update Assigned User
            Optional<User> userOptional = userService.findUserById(newAssignedUserID);
            if (userOptional.isEmpty()) {
                throw new IllegalArgumentException("Invalid User ID provided for task assignment.");
            }
            existingTask.setUser(userOptional.get());
            
            return taskRepository.save(existingTask);
        });
    }

    // --- DELETE OPERATION ---

    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }
}