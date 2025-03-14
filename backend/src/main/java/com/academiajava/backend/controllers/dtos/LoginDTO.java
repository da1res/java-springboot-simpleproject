package com.academiajava.backend.controllers.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @NotNull(message = "Password cannot be null")
    @Size(min = 3, max = 100, message 
    = "Password must be between 1 and 100 characters")
    private String password;

    @NotNull(message = "Username cannot be null")
    @Size(min = 3, max = 50, message 
      = "Username must be between 1 and 50 characters")
    @Pattern(regexp="^[a-zA-Z0-9_-]+$")
    private String username;
}
