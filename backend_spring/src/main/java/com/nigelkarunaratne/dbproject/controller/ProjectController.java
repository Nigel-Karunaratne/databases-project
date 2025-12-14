package com.nigelkarunaratne.dbproject.controller;

import com.nigelkarunaratne.dbproject.entity.Project;
import com.nigelkarunaratne.dbproject.service.ProjectService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // GET /api/projects
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.findAllProjects();
    }

    // GET /api/projects/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Integer id) {
        return projectService.findProjectById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/projects
    // Takes json body (same format as produced by GET). uses map, must manually map object
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Map<String, Object> creationRequest) {
        System.out.println("WE GOT A CREATION REQUEST");
        // extract project_name and iD
        String projectName = (String) creationRequest.get("projectName");
        Integer managerId = null;
        Object managerIdObj = creationRequest.get("managerUserID");

        if (managerIdObj instanceof Number) {
            managerId = ((Number) managerIdObj).intValue();
        } else if (managerIdObj instanceof String) {
            try {
                managerId = Integer.valueOf((String) managerIdObj);
            } catch (NumberFormatException ignored) { }
        }

        // validation
        if (projectName == null || managerId == null) {
             return ResponseEntity.badRequest().body(null);
        }

        // create a new Project model
        Project project = new Project();
        project.setProjectName(projectName);

        // Call service layer
        Optional<Project> createdProject = projectService.createProject(project, managerId);
        
        if (createdProject.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject.get());
        } else {
            // Returns 400 Bad Request if managerId was invalid
            return ResponseEntity.badRequest().body(null); 
        }
    }

    //PUT MAPPING
    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Integer projectId,
            @RequestBody Map<String, Object> updateRequest) {
        
        // similar style to post mapping
        String newProjectName = (String) updateRequest.get("projectName");
        Integer managerId = null;
        Object managerIdObj = updateRequest.get("managerUserID");
        
        if (newProjectName == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // safe conversion to int
        if (managerIdObj != null) {
            if (managerIdObj instanceof Number) {
                managerId = ((Number) managerIdObj).intValue();
            } else if (managerIdObj instanceof String) {
                try {
                    managerId = Integer.valueOf((String) managerIdObj);
                } catch (NumberFormatException ignored) { /* Handled below */ }
            }
        }
        
        // managerId must be non-null and convertible (if client sent "managerUserID": null)
        if (managerId == null) { 
            return ResponseEntity.badRequest().body(null);
        }

        // create project model (only needs the name)
        Project projectDetails = new Project();
        projectDetails.setProjectName(newProjectName); 

        try {
            return projectService.updateProject(projectId, projectDetails, managerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            // Catches invalid manager ID (non-existent in database)
            return ResponseEntity.badRequest().body(null);
        }
    }

    // DELETE /api/projects/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}