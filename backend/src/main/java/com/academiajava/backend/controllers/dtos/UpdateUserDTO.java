package com.academiajava.backend.controllers.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    private Long id;
    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 100, message 
    = "Name must be between 1 and 100 characters")
    private String name;

    private String password;
}

