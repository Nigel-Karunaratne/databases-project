package com.nigelkarunaratne.dbproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskID;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "descript", nullable = false)
    private String description;

    @Column(name = "priority_lvl", nullable = false)
    private Integer priority;

    @Column(name = "due_date", nullable = true)
    private LocalDate dueDate;

    // foreign key for projects
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference("project-tasks")
    private Project project;

    // foreign key for user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonBackReference("user-tasks")
    private User user;

    public Task() {
    }

    public Task(String title, String description, Integer priority, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    // GETTER/SETTER //

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //so that the user id is returned instead of the whole user object
    public Integer getAssignedUserID() {
    if (this.user != null) {
        return this.user.getUserID(); 
    }
    return null;
}

}