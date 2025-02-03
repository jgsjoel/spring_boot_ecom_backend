package com.ddelight.ddAPI.common.repositories;

import com.ddelight.ddAPI.common.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Roles,Long> {

    Optional<Roles> findRolesByRole(String role);

}
