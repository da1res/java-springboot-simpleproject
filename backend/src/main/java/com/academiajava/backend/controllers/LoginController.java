package com.academiajava.backend.controllers;

import com.academiajava.backend.controllers.dtos.LoginResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academiajava.backend.controllers.dtos.LoginDTO;
import com.academiajava.backend.services.AuthorizationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "")
public class LoginController {

    private final AuthorizationService authorizationService;

    public LoginController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO){
        try {
            HttpHeaders headers = new HttpHeaders();
            String token = authorizationService.login(loginDTO);
            headers.add("Authorization", "Bearer " + token);
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO(token);
            return ResponseEntity.ok().headers(headers).body(loginResponseDTO);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login.");
        }
	}
}
