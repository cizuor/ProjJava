package com.openclassroom.apiProj2.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassroom.apiProj2.model.User;
import com.openclassroom.apiProj2.model.DTO.UserDTO;
import com.openclassroom.apiProj2.repository.UserRepository;

import lombok.Data;


@Data
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserDTO getUser(final Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
             return new UserDTO(userOptional.get());
        }
        
        return null;
    }

    public List<UserDTO> getUser() {

        List<User> users = (List<User>)userRepository.findAll();
        return users.stream()
        .map(user -> new UserDTO(user)) 
        .collect(Collectors.toList());
    }

    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    public UserDTO saveUser(User user) {
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser);
    }

    public UserDTO register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return new UserDTO(userRepository.save(user));
    }

    public boolean login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) return false;
                return passwordEncoder.matches(rawPassword, userOpt.get().getPassword());
    }


    public UserDTO findByUsername(String username) {

        Optional<User> userOptional = userRepository.findByName(username);
        if(userOptional.isPresent()){
             return new UserDTO(userOptional.get());
        }
        return null;
    }
    public UserDTO findByUserEmail(String mail) {

        Optional<User> userOptional = userRepository.findByEmail(mail);
        if(userOptional.isPresent()){
             return new UserDTO(userOptional.get());
        }
        return null;
    }


}
