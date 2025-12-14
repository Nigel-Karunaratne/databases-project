package com.nigelkarunaratne.dbproject.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "managers")
public class Manager {

    @Id
    @Column(name = "user_id")
    private Integer userID;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Indicates that the primary key is mapped from the User entity
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "expertise_area", nullable = false)
    private String expertiseArea;

    @Column(name = "exp_years", nullable = false)
    private Integer experienceYears;

    // for project foreign key
    // @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Project> projects = new HashSet<>();

    public Manager() {
    }

    public Manager(String expertiseArea, Integer experienceYears) {
        this.expertiseArea = expertiseArea;
        this.experienceYears = experienceYears;
    }

    // GETTER/SETTER

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        // if (user != null) {
        //     this.userID = user.getUserID();
        // } //don't link ids here
    }

    public String getExpertiseArea() {
        return expertiseArea;
    }

    public void setExpertiseArea(String expertiseArea) {
        this.expertiseArea = expertiseArea;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    // foreign key
    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
}