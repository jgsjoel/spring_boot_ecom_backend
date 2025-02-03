package com.ddelight.ddAPI.Authentication.services;

import com.ddelight.ddAPI.common.entities.User;
import com.ddelight.ddAPI.common.repositories.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByEmail(username);
        if (!userOptional.isPresent()){
            throw new UsernameNotFoundException("User with given email not found");
        }

        return userOptional.get();
    }
}
