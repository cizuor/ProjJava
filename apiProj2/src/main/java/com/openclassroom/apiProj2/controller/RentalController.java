package com.openclassroom.apiProj2.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.apiProj2.model.Rental;
import com.openclassroom.apiProj2.service.RentalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;



    @GetMapping()
    public Map<String, List<Rental>> getRentals(){
        List<Rental> rentals = rentalService.getRental();
        return Collections.singletonMap("rentals", rentals);
    }


}
