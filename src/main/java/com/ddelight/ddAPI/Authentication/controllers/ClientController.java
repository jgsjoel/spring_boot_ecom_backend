package com.ddelight.ddAPI.Authentication.controllers;

import com.ddelight.ddAPI.Authentication.dto.LoginRequest;
import com.ddelight.ddAPI.Authentication.dto.RegisterRequest;
import com.ddelight.ddAPI.common.enums.TokenType;
import com.ddelight.ddAPI.Authentication.services.EmailSender;
import com.ddelight.ddAPI.Authentication.services.JwtService;
import com.ddelight.ddAPI.Authentication.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class ClientController {

    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;
    private final TemplateEngine templateEngine;
    private final UserService userService;
    private final JwtService jwtService;
    @Value("${email-verification-link}")
    private String verificationLinkPrefix;

    public ClientController(AuthenticationManager authenticationManager,
                            EmailSender emailSender,
                     TemplateEngine templateEngine,
                     UserService userService,
                     JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                                                    loginRequest.email(),
                                                    loginRequest.password()
                                                    ));

            Map<String,String> responseMap = new HashMap<>();
            responseMap.put("access_token",jwtService
                    .generateToken(loginRequest.email(),TokenType.ACCESS_TOKEN));
            responseMap.put("refresh_token",jwtService
                    .generateToken(loginRequest.email(),TokenType.REFRESH_TOKEN));

            return new ResponseEntity<Map<String,String>>(responseMap, HttpStatus.OK);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<List<String>>(List.of("Invalid Credintials"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){

        if(!request.password1().equals(request.password2())){
            return new ResponseEntity<>(new String[]{"passwords do not match"},HttpStatus.BAD_REQUEST);
        }

        userService.saveUser(request);

        String token = jwtService.generateToken(request.email(), TokenType.EMAIL_VERIFICATION_TOKEN);

        Context context = new Context();
        context.setVariable("name", request.firstName());
        context.setVariable("tokenizedUrl",verificationLinkPrefix+token);
        emailSender.message(templateEngine.process("email", context),request.email());
        return new ResponseEntity<>("Please check your email",HttpStatus.CREATED);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam(name = "token") String token){

        if(!jwtService.isExpired(token)){
            userService.activateUserAccount(jwtService.getEmail(token));
            return new ResponseEntity<>("Account is active",HttpStatus.OK);
        }
        return new ResponseEntity<>("Login to your account",HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> verifyRefreshToken(@RequestHeader("Refresh-Token") String refreshToken) {

        if (refreshToken == null || jwtService.isExpired(refreshToken)) {
            return ResponseEntity.status(401).body("Invalid or expired refresh token");
        }

        String newAccessToken = jwtService.generateToken(jwtService.getEmail(refreshToken), TokenType.ACCESS_TOKEN);
        String newRefreshToken = jwtService.generateToken(jwtService.getEmail(refreshToken),TokenType.REFRESH_TOKEN);

        Map<String,String> tokens = new LinkedHashMap<>();
        tokens.put("access_token", newAccessToken);
        tokens.put("refresh_token", newRefreshToken);
        return new ResponseEntity<>(tokens,HttpStatus.OK);
    }



}
