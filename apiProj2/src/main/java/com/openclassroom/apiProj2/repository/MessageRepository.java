package com.openclassroom.apiProj2.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassroom.apiProj2.model.Message;



@Repository
public interface MessageRepository extends CrudRepository<Message, Long>{

}
