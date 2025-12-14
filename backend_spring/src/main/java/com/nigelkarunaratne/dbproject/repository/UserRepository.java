package com.nigelkarunaratne.dbproject.repository;

import com.nigelkarunaratne.dbproject.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}