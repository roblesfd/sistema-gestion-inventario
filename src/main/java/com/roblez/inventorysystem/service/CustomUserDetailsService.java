package com.roblez.inventorysystem.service;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.roblez.inventorysystem.domain.User;
import com.roblez.inventorysystem.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        var authorities = user.getRoles().stream()
            .map(r -> "ROLE_" + r.getName()) // opcional prefijo ROLE_
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User.builder() //Para evitar conflictos de nombres con modelo User
            .username(user.getUsername())
            .password(user.getPassword())
            .authorities(authorities)
            .accountLocked(!Boolean.TRUE.equals(user.isActive()))
            .build();
    }
}