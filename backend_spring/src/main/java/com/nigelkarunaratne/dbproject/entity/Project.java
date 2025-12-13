package com.nigelkarunaratne.dbproject.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
// import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @Column(name = "project_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectID;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    // for manager
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_uid", referencedColumnName = "user_id", nullable = true)
    @JsonBackReference
    private Manager manager;

    // for task
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonManagedReference("project-tasks")
    @JsonIgnore
    private Set<Task> tasks = new HashSet<>();

    
    public Project() {
        
    }
    
    public Project(String projectName) {
        this.projectName = projectName;
    }

    // GETTERS/SETTERS //

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    // for manager
    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    // for task
    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

}
