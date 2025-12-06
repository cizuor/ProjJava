package com.openclassroom.apiProj2.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @GetMapping()
    public Map<String, List<Rental>> getRentals(){
        List<Rental> rentals = rentalService.getRental();
        return Collections.singletonMap("rentals", rentals);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addRental(@RequestParam("name") String name,
            @RequestParam("surface") Long surface,
            @RequestParam("price") Long price,
            @RequestParam("description") String description,
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

}
