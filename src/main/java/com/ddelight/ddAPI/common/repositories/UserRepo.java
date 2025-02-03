package com.ddelight.ddAPI.common.repositories;

import com.ddelight.ddAPI.common.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepo extends JpaRepository<User,Long> {

    public Optional<User> findByEmail(String email);

}
