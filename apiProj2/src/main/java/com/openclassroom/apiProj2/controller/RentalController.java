package com.openclassroom.apiProj2.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.openclassroom.apiProj2.model.Rental;
import com.openclassroom.apiProj2.model.User;
import com.openclassroom.apiProj2.model.DTO.UserDTO;
import com.openclassroom.apiProj2.service.FileService;
import com.openclassroom.apiProj2.service.RentalService;
import com.openclassroom.apiProj2.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rentals", description = "gestion des locations (CRUD)")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

     @Operation(
        summary = "Obtenir toutes les locations",
        description = "Retourne la liste complète des locations disponibles."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des locations renvoyée")
    })
    @GetMapping()
    public Map<String, List<Rental>> getRentals(){
        List<Rental> rentals = rentalService.getRental();
        return Collections.singletonMap("rentals", rentals);
    }

    @Operation(
        summary = "Créer une nouvelle location",
        description = "Ajoute une nouvelle location. Nécessite un token JWT."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Location créée"),
        @ApiResponse(responseCode = "401", description = "Authentification requise ou invalide")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addRental(
        @Parameter(description = "Nom de la location") 
        @RequestParam("name") String name,
        @Parameter(description = "Surface en m²") 
        @RequestParam("surface") Long surface,
        @Parameter(description = "Prix en euros") 
        @RequestParam("price") Long price,
        @Parameter(description = "Description détaillée") 
        @RequestParam("description") String description,
         @Parameter(description = "Photo associée", content = @Content(mediaType = "image/jpeg"))
        @RequestParam("picture") MultipartFile picture,
        Authentication authentication){
            try {
                String pictureUrl = fileService.saveFile(picture);
                String nom = authentication.getName();
                UserDTO user = userService.findByUsername(nom);

                Rental rental = new Rental();
                rental.setName(name);
                rental.setSurface(surface);
                rental.setPrice(price);
                rental.setDescription(description);
                rental.setPicture(pictureUrl);
                rental.setOwner_id(user.getId());
                

                rentalService.saveRental(rental);

                return ResponseEntity.ok(Map.of("message", "Rental created !"));
            }catch(Exception e){
                e.printStackTrace();
                return ResponseEntity.status(401).body(Map.of("error", "Error while creating rental"));
            }
    }

    @Operation(
        summary = "Mettre à jour une location",
        description = "Modifie une location existante. Nécessite un token JWT."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Location mise à jour"),
        @ApiResponse(responseCode = "401", description = "Authentification requise ou invalide"),
        @ApiResponse(responseCode = "404", description = "Location introuvable")
    })
    @PutMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateRental(
        @Parameter(description = "ID de la location à modifier")
        @PathVariable Long id,
        @Parameter(description = "Nom de la location")
            @RequestParam("name") String name,
            @Parameter(description = "Surface en m²")
            @RequestParam("surface") Long surface,
            @Parameter(description = "Prix en euros")
            @RequestParam("price") Long price,
            @Parameter(description = "Description de la location")
            @RequestParam("description") String description,
            Authentication authentication){
        try {
            String nom = authentication.getName();
            UserDTO user = userService.findByUsername(nom);
            Rental rental = new Rental();
            rental.setId(id);
            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);
            rental.setOwner_id(user.getId());
            

            rentalService.updateRental(rental);

            return ResponseEntity.ok(Map.of("message", "Rental updated !"));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(401).body(Map.of("error", "Error while updating rental"));
        }
    }


    @Operation(
        summary = "Récupérer une location par son ID",
        description = "Retourne les détails d'une location spécifique."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Location trouvée"),
        @ApiResponse(responseCode = "404", description = "Location non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRental(@Parameter(description = "ID du rental recherché") @PathVariable Long id){
        Optional<Rental> rentalOptional = rentalService.getRental(id);
        if(rentalOptional.isPresent()){
            return ResponseEntity.ok(rentalOptional.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }




}
