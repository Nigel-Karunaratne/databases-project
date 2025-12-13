package com.nigelkarunaratne.dbproject.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "addr", nullable = true)
    private String addr;

    // For Manager using foreign key
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Manager managerProfile;

    // For Task using foreign key
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-tasks")
    private Set<Task> assignedTasks = new HashSet<>();

    public User() {
    }

    public User(String firstName, String lastName, String email, String addr) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.addr = addr;
    }

    // GETTERS/SETTERS //

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    // for manager
    public Manager getManagerProfile() {
        return managerProfile;
    }

    public void setManagerProfile(Manager managerProfile) {
        this.managerProfile = managerProfile;
        // set bidirectional links
        if (managerProfile != null) {
            managerProfile.setUser(this);
        }
    }
    
    // for task
    public Set<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(Set<Task> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }
}