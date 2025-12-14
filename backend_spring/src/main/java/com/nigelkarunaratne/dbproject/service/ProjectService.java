package com.nigelkarunaratne.dbproject.service;

import com.nigelkarunaratne.dbproject.entity.Project;
import com.nigelkarunaratne.dbproject.entity.Manager;
import com.nigelkarunaratne.dbproject.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ManagerService managerService; // For finding the associated Manager

    public ProjectService(ProjectRepository projectRepository, ManagerService managerService) {
        this.projectRepository = projectRepository;
        this.managerService = managerService;
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> findProjectById(Integer id) {
        return projectRepository.findById(id);
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public Optional<Project> createProject(Project project, Integer managerId) {
        // requires manager id now
        if (managerId == null) {
            return Optional.empty();
        }
        
        Optional<Manager> managerOptional = managerService.findManagerById(managerId);

        if (managerOptional.isPresent()) {
            project.setManager(managerOptional.get());
            return Optional.of(projectRepository.save(project));
        } else {
            return Optional.empty(); // Manager not found
        }
    }

    public void deleteProject(Integer id) {
        projectRepository.deleteById(id);
    }

    @Transactional
    public Optional<Project> updateProject(Integer projectId, Project projectDetails, Integer managerId) {
        
        return projectRepository.findById(projectId).map(existingProject -> {
            
            existingProject.setProjectName(projectDetails.getProjectName());
            
            // manager reassignment
            if (managerId != null) {
                // If a new manager ID is provided, try to assign it
                Optional<Manager> managerOptional = managerService.findManagerById(managerId);
                
                if (managerOptional.isPresent()) {
                    existingProject.setManager(managerOptional.get());
                } else {
                    // Manager ID was provided but invalid. 
                    // do not return empty (implies updsate failed due to improper input)
                    throw new IllegalArgumentException("Cannot update project: Invalid Manager ID provided.");
                }
            } 
            // If managerId is null, we DO NOTHING, so existing manager is kept.            
            return projectRepository.save(existingProject);
        });
    }
}