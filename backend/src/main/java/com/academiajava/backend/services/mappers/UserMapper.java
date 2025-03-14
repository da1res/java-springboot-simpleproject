package com.academiajava.backend.services.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.academiajava.backend.controllers.dtos.AddUserDTO;
import com.academiajava.backend.domain.User;
import com.academiajava.backend.services.dtos.UserDTO;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public User addUserToEntity(AddUserDTO addUserDTO){
        return modelMapper.map(addUserDTO, User.class);
    }

    public UserDTO entityToDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}
