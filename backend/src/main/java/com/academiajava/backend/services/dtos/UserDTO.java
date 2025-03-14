package com.academiajava.backend.services.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private Long id;
    private String name;
    private String username;
    private LocalDateTime creation;
    private LocalDateTime lastUpdated;
    
}
