package com.ddelight.ddAPI.account.controllers;

import com.ddelight.ddAPI.Authentication.services.UserService;
import com.ddelight.ddAPI.account.dto.AddressRequest;
import com.ddelight.ddAPI.account.dto.AddressResponse;
import com.ddelight.ddAPI.account.dto.UpdateRequest;
import com.ddelight.ddAPI.account.dto.UserResponse;
import com.ddelight.ddAPI.common.entities.Address;
import com.ddelight.ddAPI.common.entities.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private UserService userService;

    public AccountController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(){
        User user = userService.getUserDetails();

        return new ResponseEntity<>(new UserResponse(
                user.getFirstName(),
                user.getLaseName(),
                user.getEmail(),
                user.getMobile()
        ), HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateRequest request){
        userService.updateUser(request);
        return new ResponseEntity<>("Update Complete",HttpStatus.OK);
    }





}
