package com.academiajava.backend.services;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.academiajava.backend.controllers.dtos.LoginDTO;
import com.academiajava.backend.domain.User;
import com.academiajava.backend.repositories.UserRepository;
import com.academiajava.backend.security.JwtService;

@Service
public class AuthorizationService implements UserDetailsService {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthorizationService(AuthenticationConfiguration authenticationConfiguration, JwtService jwtService, UserRepository userRepository) throws Exception {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public String login(LoginDTO loginDTO) throws Exception {
        var auth = authenticationConfiguration.getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        return jwtService.generateToken((User) auth.getPrincipal());
    }

}
