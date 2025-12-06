package com.openclassroom.apiProj2.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openclassroom.apiProj2.model.User;
import com.openclassroom.apiProj2.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Chercher l'utilisateur dans la BDD
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom : " + username));

        // 2. Retourner un objet User de Spring Security (pas ton entité User !)
        // On map les données de ton entité vers l'objet attendu par Spring
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                // Ici on définit les rôles/autorités (vide pour l'instant ou basé sur user.getRole())
                Collections.emptyList() 
        );
    }

    public User findEntityByUsername(String username) {
    return userRepository.findByName(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
