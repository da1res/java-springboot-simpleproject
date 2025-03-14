package com.academiajava.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.academiajava.backend.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.academiajava.backend.controllers.dtos.AddUserDTO;
import com.academiajava.backend.controllers.dtos.UpdateUserDTO;
import com.academiajava.backend.domain.User;
import com.academiajava.backend.repositories.UserRepository;
import com.academiajava.backend.security.UserPermissionService;
import com.academiajava.backend.services.dtos.UserDTO;
import com.academiajava.backend.services.mappers.UserMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;


	@Autowired
	public UserService(UserRepository userRepository, UserMapper userMapper, UserPermissionService userPermissionService){
		this.userRepository = userRepository;
		this.userMapper = userMapper; 
	}

	public List<UserDTO> getUsers(String name, String username){

		List<User> usersList = null;

		if(name != null && username != null){
			usersList = userRepository.findAllByNameContainingIgnoreCaseAndUsernameContainingIgnoreCase(name, username);
		}else if(name != null){
			usersList = userRepository.findByNameContainsIgnoreCase(name);
		}else if(username != null){
			usersList = userRepository.findByUsernameContainsIgnoreCase(username);
		}else {
			usersList = userRepository.findAll();
		}
		
		if(usersList != null){
			return usersList.stream().map(userMapper::entityToDTO).collect(Collectors.toList());
		}

		return List.of();
	}

	public UserDTO getUser(Long id){

		if(id == null){
			throw new IllegalStateException("Id can't be null!");
		}

		Optional<User> userFound = userRepository.findById(id);

		if(userFound.isPresent()){
			UserDTO userDTO = userMapper.entityToDTO(userFound.get());;
			return userDTO;
		}else {
			throw new NoSuchElementException("User with given id doesn't exist!");
		}

	}

	public void deleteUser(Long id){

		if(id == null){
			throw new IllegalArgumentException("Id can't be null!");
		}

		if(userRepository.existsById(id)){
			userRepository.deleteById(id);
		}else{
			throw new EntityNotFoundException("User with given id doesn't exist!");
		}

	}

    public UserDTO addUser(AddUserDTO addUserDTO) {
        // Validate if the username already exists
		Optional<User> userOptional = userRepository.findUserByUsername(addUserDTO.getUsername());
		if(userOptional.isPresent()){
			throw new IllegalStateException("Username already in use!");
		}

		String encryptedPassword = new BCryptPasswordEncoder().encode(addUserDTO.getPassword());

		// Mapping from AddUserDTO to User Entity
		User userToSave = userMapper.addUserToEntity(addUserDTO);
		userToSave.setCreation(LocalDateTime.now());
		userToSave.setLastUpdated(null);
		userToSave.setPassword(encryptedPassword);
		userToSave.setRole(Role.ROLE_USER);

		// User Entity database saving
		User savedUser = userRepository.save(userToSave);

		// Mapping from User Entity to UserDTO and returning the dto
		return userMapper.entityToDTO(savedUser);
    }

	@Transactional
    public void updateUser(UpdateUserDTO user) {
        Long idToUpdate = user.getId();

        User userToUpdate = userRepository.findById(idToUpdate).orElseThrow(() -> new EntityNotFoundException("User with id " + idToUpdate + "does not exists!"));
		
		String nameToUpdate = user.getName();


		if((nameToUpdate != null && !nameToUpdate.isEmpty() && !Objects.equals(userToUpdate.getName(), nameToUpdate))
		&& (user.getPassword() != null && !user.getPassword().isEmpty())  ){
			userToUpdate.setName(nameToUpdate);
			String encryptedPasswordToUpdate = new BCryptPasswordEncoder().encode(user.getPassword());
			userToUpdate.setPassword(encryptedPasswordToUpdate);
		}

		userToUpdate.setLastUpdated(LocalDateTime.now());

    }

}
