package com.academiajava.backend.controllers.dtos;

import com.academiajava.backend.domain.Role;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserDTO {
    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 100, message 
    = "Name must be between 1 and 100 characters")
    private String name;

    @NotNull(message = "Username cannot be null")
    @Size(min = 3, max = 50, message 
      = "Username must be between 1 and 50 characters")
    @Pattern(regexp="^[a-zA-Z0-9]+$")
    private String username;

    private String password;

    private Role role;
}
