package com.nigelkarunaratne.dbproject.controller;

import com.nigelkarunaratne.dbproject.entity.Task;
import com.nigelkarunaratne.dbproject.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.findAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.findTaskById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/tasks?projectId=1&userId=2
    // Creates a new task and assigns it to the specified project and optional user.
    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody Task task, 
            @RequestParam Long projectId, // Required
            @RequestParam(required = false) Long userId) { // Optional
        
        Optional<Task> createdTask = taskService.createTask(task, projectId, userId);

        if (createdTask.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask.get());
        } else {
            // Returns 400 Bad Request if the required projectId was invalid
            return ResponseEntity.badRequest().build(); 
        }
    }
    
    // PUT /api/tasks/1
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        return taskService.updateTask(id, taskDetails)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/tasks/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.deleteTask(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}