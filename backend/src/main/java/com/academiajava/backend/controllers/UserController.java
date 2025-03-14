package com.academiajava.backend.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.academiajava.backend.controllers.dtos.AddUserDTO;
import com.academiajava.backend.controllers.dtos.UpdateUserDTO;
import com.academiajava.backend.security.UserPermissionService;
import com.academiajava.backend.services.UserService;
import com.academiajava.backend.services.dtos.UserDTO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
	private final UserPermissionService userPermissionService;

    @Autowired
    public UserController(UserService userService, UserPermissionService userPermissionService){
        this.userService = userService;
		this.userPermissionService = userPermissionService;
    }

	/* DONE */
	@GetMapping
	public ResponseEntity<?> getUsers(@RequestParam(value ="name", required = false) String name
	, @RequestParam(value = "username", required = false) String username){
			List<UserDTO> users = userService.getUsers(name, username);

			if (users == null) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
			}
			
			return ResponseEntity.ok(users);
	}

	/* DONE */
    @GetMapping(path = "/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") Long id){
		return ResponseEntity.ok(this.userService.getUser(id));
	}

	/* DONE */
    @PostMapping
	public ResponseEntity<?> addUser(@RequestBody @Valid AddUserDTO addUserDTO, Authentication authentication){
		if (!userPermissionService.canAddUser(authentication)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: You don't have permission to add users.");
		}
		UserDTO userDTO = userService.addUser(addUserDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
	}

	/* DONE */
    @DeleteMapping(path = "/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Long id, Authentication authentication){
		if (!userPermissionService.canDeleteUser(authentication)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: You don't have permission to delete users.");
		}
		this.userService.deleteUser(id);
		return ResponseEntity.ok().body("User deleted!");
	}
	
	/* DONE */
    @PutMapping
	public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO updateUserDTO, Authentication authentication){
		if (!userPermissionService.canUpdateUser(authentication)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		this.userService.updateUser(updateUserDTO);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
