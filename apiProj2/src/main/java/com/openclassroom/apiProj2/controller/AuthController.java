package com.openclassroom.apiProj2.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.apiProj2.model.User;
import com.openclassroom.apiProj2.security.JwtUtils;
import com.openclassroom.apiProj2.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService; 

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.findByUsername(user.getName()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if(userService.findByUserEmail(user.getEmail()) != null){
            return ResponseEntity.badRequest().body("email already used");
        }
        
        User savedUser = userService.register(user);
        String token = jwtUtils.generateToken(savedUser.getName());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        boolean success = userService.login(user.getName(), user.getPassword());
        if (success) {
            String token = jwtUtils.generateToken(user.getName());
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(401).body("Login failed");
        }
    }



    /*@GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        // Retourne les infos de l'utilisateur connecté
    }*/

}
