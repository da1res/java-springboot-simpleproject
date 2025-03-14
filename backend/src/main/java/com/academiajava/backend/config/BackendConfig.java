package com.academiajava.backend.config;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.academiajava.backend.domain.User;
import com.academiajava.backend.repositories.UserRepository;

@Configuration
public class BackendConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository){
        return args -> {
            User daniel = new User(
                "Daniel Aires",
                "daires", 
                LocalDateTime.now(),
                LocalDateTime.now(),
                new BCryptPasswordEncoder().encode("ola"),
                "ROLE_ADMIN"
                );


            User joana = new User(
                "Joana Fernandes",
                "jfernandes",
                LocalDateTime.now(),
                LocalDateTime.now(),
                new BCryptPasswordEncoder().encode("ola"),
                "ROLE_USER"
                );

            userRepository.saveAll(List.of(daniel, joana));
        };
    };
}

