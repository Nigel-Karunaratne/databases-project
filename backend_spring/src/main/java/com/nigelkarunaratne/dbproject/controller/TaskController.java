package com.nigelkarunaratne.dbproject.controller;

import com.nigelkarunaratne.dbproject.entity.Task;
import com.nigelkarunaratne.dbproject.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // --- Helper Method to Extract and Validate Fields (Used by POST and PUT) ---
    private ResponseEntity<Task> processTaskRequest(Map<String, Object> request, Integer taskId) {
        
        // 1. EXTRACT AND CONVERT ALL REQUIRED FIELDS
        String title = (String) request.get("title");
        String description = (String) request.get("description");
        
        Integer priority = null;
        Object priorityObj = request.get("priority");
        if (priorityObj instanceof Number) { priority = ((Number) priorityObj).intValue(); } 
        
        LocalDate dueDate = null;
        Object dueDateObj = request.get("dueDate");
        if (dueDateObj instanceof String) { 
             try { dueDate = LocalDate.parse((String) dueDateObj); } catch (Exception ignored) { } 
        }
        
        Integer newAssignedUserID = null;
        Object userIdObj = request.get("assignedUserID"); // Assuming user_id/project_id are the client keys now
        if (userIdObj instanceof Number) { newAssignedUserID = ((Number) userIdObj).intValue(); } 
        
        Integer newProjectID = null;
        Object projectIdObj = request.get("projectID");
        if (projectIdObj instanceof Number) { newProjectID = ((Number) projectIdObj).intValue(); } 

        // 2. VALIDATION (All fields are required)
        if (title == null || description == null || priority == null || dueDate == null || 
            newAssignedUserID == null || newProjectID == null) {
            return ResponseEntity.badRequest().body(null); 
        }

        // 3. CONSTRUCT THE TASK ENTITY
        Task taskDetails = new Task();
        if (taskId != null) { taskDetails.setTaskID(taskId); } // Set ID for POST/PUT consistency
        taskDetails.setTitle(title);
        taskDetails.setDescription(description);
        taskDetails.setPriority(priority);
        taskDetails.setDueDate(dueDate);

        try {
            // 4. CALL THE SERVICE
            if (taskId == null) { // POST (Create)
                return taskService.createTask(taskDetails, newProjectID, newAssignedUserID)
                    .map(task -> ResponseEntity.status(HttpStatus.CREATED).body(task))
                    .orElseGet(() -> ResponseEntity.badRequest().body(null));
            } else { // PUT (Update)
                return taskService.updateTask(taskId, taskDetails, newProjectID, newAssignedUserID)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
            }
        } catch (IllegalArgumentException e) {
            // Catches invalid IDs (non-existent Project or User) from the service layer
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // --- GET OPERATIONS ---

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Integer id) {
        return taskService.findTaskById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // --- POST OPERATION ---

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Map<String, Object> creationRequest) {
        // taskId is null for creation
        return processTaskRequest(creationRequest, null); 
    }

    // --- PUT OPERATION ---

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updateRequest) {
        
        return processTaskRequest(updateRequest, id);
    }

    // --- DELETE OPERATION ---

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        if (taskService.findTaskById(id).isPresent()) {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}