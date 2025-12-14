package com.nigelkarunaratne.dbproject.repository;

import com.nigelkarunaratne.dbproject.entity.Manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    // Possible TO do - add custom queries here? EX: List<Manager> findByExpertiseArea(String area);
}