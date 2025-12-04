package com.openclassroom.apiProj2.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassroom.apiProj2.model.Rental;



@Repository
public interface RentalRepository extends CrudRepository<Rental, Long> {

}
