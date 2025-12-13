package com.nigelkarunaratne.dbproject.controller;

import com.nigelkarunaratne.dbproject.entity.Project;
import com.nigelkarunaratne.dbproject.service.ProjectService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return projectService.findProjectById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/projects
    // Takes json body (same format as produced by GET)
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project, @RequestParam(required = false) Long managerId) {
        // If a managerId is provided, try to assign it
        if (managerId != null) {
            Optional<Project> createdProject = projectService.createProject(project, managerId);
            if (createdProject.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdProject.get());
            } else {
                // If managerId was provided but not found
                return ResponseEntity.badRequest().body(null); 
            }
        }
        
        // If no managerId is provided (or manager not found) but project creation is allowed
        Project newProject = projectService.saveProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProject);
    }

    // DELETE /api/projects/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}