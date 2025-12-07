package com.openclassroom.apiProj2.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Gestion de l'authentification et des utilisateurs")
public class AuthController {
    
    @Autowired
    private UserService userService; 

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;


    @Operation(
        summary = "Inscrire un nouvel utilisateur",
        description = "Ajoute un nouvel utilisateur à la base de données et retourne un token JWT."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Utilisateur créé avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"token\": \"eyJhbGciOiJIUzI...\"}"))
        ),
        @ApiResponse(responseCode = "400", description = "Requête invalide (Email ou Username déjà existant)", content = @Content)
    })
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


    @Operation(
        summary = "Connexion utilisateur",
        description = "Authentifie un utilisateur via email et mot de passe pour obtenir un token JWT."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Identifiants de l'utilisateur",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(example = "{\"email\": \"test@test.com\", \"password\": \"password123\"}")
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Connexion réussie",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"token\": \"eyJhbGciOiJIUzI...\"}"))
        ),
        @ApiResponse(responseCode = "400", description = "Champs manquants", content = @Content),
        @ApiResponse(responseCode = "401", description = "Authentification échouée (Mauvais identifiants)", content = @Content)
    })
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


    @Operation(
        summary = "Récupérer l'utilisateur courant",
        description = "Renvoie les informations de l'utilisateur connecté basé sur le token JWT fourni dans le header.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Informations de l'utilisateur retournées",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john@test.com\"}"))
        ),
        @ApiResponse(responseCode = "401", description = "Token invalide ou manquant", content = @Content),
        @ApiResponse(responseCode = "404", description = "Utilisateur introuvable", content = @Content)
    })
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


        Map<String, Object> userInfo = Map.of(
            "id", user.getId(),
            "name", user.getName(),
            "email", user.getEmail()
        );

        return ResponseEntity.ok(userInfo);
    }

}
