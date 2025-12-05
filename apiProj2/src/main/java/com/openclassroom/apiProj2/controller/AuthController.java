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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.apiProj2.model.User;
import com.openclassroom.apiProj2.model.DTO.UserDTO;
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
            return ResponseEntity.badRequest().body("{\"error\":\"Username already exists\"}");
        }
        if(userService.findByUserEmail(user.getEmail()) != null){
            return ResponseEntity.badRequest().body("{\"error\":\"Email already used\"}");
        }
        
        UserDTO savedUser = userService.register(user);
        String token = jwtUtils.generateToken(savedUser.getName());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null) {
            return ResponseEntity.status(400).body("{}");
        }

        boolean success = userService.login(email, password);
        if (success) {
            String token = jwtUtils.generateToken(userService.findByUserEmail(email).getName());
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(401).body("{\"message\": \"error\"}");
        }
    }



    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        if (authHeader  == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("{}");
        }

        String token = authHeader.substring(7); // enlève "Bearer "

        String username;
        try {
            username = jwtUtils.extractUsername(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("{}");
        }

        UserDTO user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("{}");
        }


        // c'est pas un DTO mais c'est tout comme? 
        //pour un projet minimaliste je pense que cela suffi
        Map<String, Object> userInfo = Map.of(
            "id", user.getId(),
            "name", user.getName(),
            "email", user.getEmail()
        );

        return ResponseEntity.ok(userInfo);
    }

}
