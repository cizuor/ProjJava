package com.openclassroom.apiProj2.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.apiProj2.model.Message;
import com.openclassroom.apiProj2.service.MessageService;
import com.openclassroom.apiProj2.service.UserService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;


    @PostMapping()
    public ResponseEntity<?> addMessage(@RequestBody Map<String, String> body){
        String rentalid = body.get("rental_id");
        String userid = body.get("user_id");
        String txtmessage = body.get("message");

        if (rentalid == null || userid == null || txtmessage == null) {
            return ResponseEntity.status(400).body("{}");
        }

        long uid = Long.parseLong(userid);
        long rentid = Long.parseLong(rentalid);
        Message message = new Message();
        message.setMessage(txtmessage);
        message.setRental_id(rentid);
        message.setUser_id(uid);
        messageService.saveMessage(message);

        return ResponseEntity.ok(Map.of("message", "Message send with success"));

    }


}
