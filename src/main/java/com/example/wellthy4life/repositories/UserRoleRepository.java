package com.example.wellthy4life.repositories;

import com.example.wellthy4life.models.UserRole;
import com.example.wellthy4life.models.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {}
