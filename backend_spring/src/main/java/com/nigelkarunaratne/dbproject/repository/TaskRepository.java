package com.nigelkarunaratne.dbproject.repository;

import com.nigelkarunaratne.dbproject.entity.Task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUserUserID(Integer userID); //custom
}