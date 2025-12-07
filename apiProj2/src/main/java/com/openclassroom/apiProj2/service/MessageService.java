package com.openclassroom.apiProj2.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassroom.apiProj2.model.Message;
import com.openclassroom.apiProj2.repository.MessageRepository;

import lombok.Data;

@Data
@Service
public class MessageService {


    @Autowired
    private MessageRepository  messageRepository;

    public Optional<Message> getMessage(final long id){
        return messageRepository.findById(id);
    }

    public Message saveMessage(Message message){
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);
        return savedMessage;
    }


}
