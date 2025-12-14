package com.nigelkarunaratne.dbproject.repository;

import com.nigelkarunaratne.dbproject.entity.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
