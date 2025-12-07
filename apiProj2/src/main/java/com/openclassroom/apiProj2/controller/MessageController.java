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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "gestion des message ")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Operation(
        summary = "Ajouter un nouveau message",
        description = "Ajoute un nouveau message à la base de données."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Détails du message à envoyer",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(example = "{\"rental_id\": \"1\", \"user_id\": \"1\", \"message\": \"Bonjour, je suis intéressé !\"}")
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Message créé avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Message send with success\"}"))
        ),
        @ApiResponse(responseCode = "400", description = "Requête invalide (iduser,idrental ou message vide)", content = @Content)
    })
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
