package com.openclassroom.apiProj2.controller;

import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.apiProj2.model.DTO.UserDTO;
import com.openclassroom.apiProj2.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "gestion des utilisateur ")
public class UserController {

    @Autowired
    private UserService userService;


    @Operation(
        summary = "Récupérer un user par son ID",
        description = "Retourne les détails d'un user spécifique."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User trouvée"),
        @ApiResponse(responseCode = "404", description = "User non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@Parameter(description = "ID du user recherché") @PathVariable Long id) {
        UserDTO userDto = userService.getUser(id);
        
        if (userDto == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(userDto);
    } 

}
