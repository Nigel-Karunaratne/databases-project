package com.nigelkarunaratne.dbproject.service;

import com.nigelkarunaratne.dbproject.entity.Task;
import com.nigelkarunaratne.dbproject.entity.Project;
import com.nigelkarunaratne.dbproject.entity.User;
import com.nigelkarunaratne.dbproject.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, ProjectService projectService, UserService userService) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
        this.userService = userService;
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // Method to create a task, linking it to Project (required) and User (optional)
    public Optional<Task> createTask(Task task, Long projectId, Long userId) {
        Optional<Project> projectOptional = projectService.findProjectById(projectId);

        if (projectOptional.isEmpty()) {
            return Optional.empty(); // Project not found, cannot create task
        }

        task.setProject(projectOptional.get());

        if (userId != null) {
            Optional<User> userOptional = userService.findUserById(userId);
            // If the user exists, set them as the assignee; otherwise, assignee remains null.
            userOptional.ifPresent(task::setUser);
        }
        
        return Optional.of(taskRepository.save(task));
    }

    public Optional<Task> updateTask(Long id, Task taskDetails) {
        return taskRepository.findById(id).map(existingTask -> {
            // Update core task properties
            existingTask.setTitle(taskDetails.getTitle());
            existingTask.setDescription(taskDetails.getDescription());
            existingTask.setPriority(taskDetails.getPriority());
            existingTask.setDueDate(taskDetails.getDueDate());
            
            // Note: Relationship updates (e.g., changing project/assignee) would require 
            // separate logic checking for new IDs, but basic property updates are handled here.

            return taskRepository.save(existingTask);
        });
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}