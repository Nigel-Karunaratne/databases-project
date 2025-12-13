package com.nigelkarunaratne.dbproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "managers")
public class Manager {

    @Id
    @Column(name = "user_id")
    private Long userID;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Indicates that the primary key is mapped from the User entity
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(nullable = false)
    private String expertise_area;

    @Column(nullable = false)
    private Integer exp_years;

    public Manager() {
    }

    public Manager(String expertise_area, Integer exp_years) {
        this.expertise_area = expertise_area;
        this.exp_years = exp_years;
    }

    // GETTER/SETTER

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public User getUser() {
        return user;
    }

    // When setting the User, we also link the IDs
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userID = user.getUserID();
        }
    }

    public String getExpertise_area() {
        return expertise_area;
    }

    public void setExpertise_area(String expertise_area) {
        this.expertise_area = expertise_area;
    }

    public Integer getExp_years() {
        return exp_years;
    }

    public void setExp_years(Integer exp_years) {
        this.exp_years = exp_years;
    }
}