package com.openclassroom.apiProj2.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassroom.apiProj2.model.Rental;
import com.openclassroom.apiProj2.model.DTO.UserDTO;
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

    public Rental updateRental(Rental rentalUpdated){

        rentalUpdated.setUpdatedAt(LocalDateTime.now());
        Optional<Rental> rentalOptional = rentalRepository.findById(rentalUpdated.getId());
        Rental rental;
        if(rentalOptional.isPresent()){
            rental = rentalOptional.get();
            // juste au cas ou en bidouillant quelqun arrive a envoyer une requette interdite.
            if(rental.getOwner_id() == rentalUpdated.getOwner_id()){
                rental.setUpdatedAt(LocalDateTime.now());
                rental.setName(rentalUpdated.getName());
                rental.setSurface(rentalUpdated.getSurface());
                rental.setPrice(rentalUpdated.getPrice());
                rental.setDescription(rentalUpdated.getDescription());

                // fonction aurai juste pu faire 
                // rentalUpdated.setUpdatedAt(LocalDateTime.now());
                // return rentalRepository.save(rentalUpdated);
                // mais risque si on utilise des DTO ou si on ne recupère pas certain champ
                Rental savedRental = rentalRepository.save(rental);
                return savedRental;
            }
            else{
                return null;
            }
            

        }else{
            return null;
        }
    }


}
