package com.openclassroom.apiProj2.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassroom.apiProj2.model.Rental;
import com.openclassroom.apiProj2.repository.RentalRepository;


import lombok.Data;

@Data
@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    public Optional<Rental> getRental(final Long id){
        return rentalRepository.findById(id);
    }

    public List<Rental> getRental(){
        return (List<Rental>) rentalRepository.findAll();
    }

    public void deleteUser(final Long id){
        rentalRepository.deleteById(id);
    }

    public Rental saveRental(Rental rental){
        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());
        Rental savedRental = rentalRepository.save(rental);
        return savedRental;
    }



}
