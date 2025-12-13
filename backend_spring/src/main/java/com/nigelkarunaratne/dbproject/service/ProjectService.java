package com.nigelkarunaratne.dbproject.service;

import com.nigelkarunaratne.dbproject.entity.Project;
import com.nigelkarunaratne.dbproject.entity.Manager;
import com.nigelkarunaratne.dbproject.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ManagerService managerService; // To find the associated Manager

    public ProjectService(ProjectRepository projectRepository, ManagerService managerService) {
        this.projectRepository = projectRepository;
        this.managerService = managerService;
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> findProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    // Method to create/update a project and assign a manager
    public Optional<Project> createProject(Project project, Long managerId) {
        Optional<Manager> managerOptional = managerService.findManagerById(managerId);

        if (managerOptional.isPresent()) {
            Manager manager = managerOptional.get();
            project.setManager(manager);
            return Optional.of(projectRepository.save(project));
        } else {
            // Can choose to save the project without a manager (as the FK is nullable)
            // For now, we only create if the manager exists.
            return Optional.empty(); // Manager not found

            // return Optional.of(projectRepository.save(project));
        }
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}