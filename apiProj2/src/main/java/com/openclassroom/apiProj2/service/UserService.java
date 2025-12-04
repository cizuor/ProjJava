package com.openclassroom.apiProj2.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassroom.apiProj2.model.User;
import com.openclassroom.apiProj2.repository.UserRepository;

import lombok.Data;


@Data
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Optional<User> getUser(final Long id) {
        return userRepository.findById(id);
    }

    public Iterable<User> getUser() {
        return userRepository.findAll();
    }

    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) return false;
                return passwordEncoder.matches(rawPassword, userOpt.get().getPassword());
    }


    public User findByUsername(String username) {
        return userRepository.findByName(username).orElse(null);
    }
    public User findByUserEmail(String mail) {
        return userRepository.findByEmail(mail).orElse(null);
    }


}
