package com.openclassroom.apiProj2.controller;

import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.apiProj2.model.DTO.UserDTO;
import com.openclassroom.apiProj2.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDto = userService.getUser(id);
        
        if (userDto == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(userDto);
    } 

/*{
  "id": 2,
  "name": "Owner Name",
  "email": "test@test.com",
  "created_at": "2022/02/02",
  "updated_at": "2022/08/02"
}
 */
}
