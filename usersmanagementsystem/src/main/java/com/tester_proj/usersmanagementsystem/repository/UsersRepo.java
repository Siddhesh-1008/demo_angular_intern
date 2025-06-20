package com.tester_proj.usersmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tester_proj.usersmanagementsystem.entity.OurUsers;

public interface UsersRepo extends JpaRepository<OurUsers, Integer> {
  // The UsersRepo interface extends JpaRepository, which provides built-in methods
  // for performing database operations such as saving, deleting, updating, and finding records.
  // It works with the 'OurUsers' entity and uses 'Integer' as the type of its primary key.

  Optional<OurUsers> findByEmail(String email);
  // This method is a custom query method that finds a user by their email.
  // It returns an Optional, which means the result could be a user or empty (if no user is found).
  // Spring Data JPA automatically implements this method based on its name.
}
