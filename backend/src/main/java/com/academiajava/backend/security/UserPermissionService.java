package com.academiajava.backend.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.academiajava.backend.domain.User;
import com.academiajava.backend.repositories.UserRepository;

@Service
public class UserPermissionService {
    
    @Autowired
    UserRepository userRepository;

    public boolean canUpdateUser(Authentication authentication){
        String loggedInUsername = authentication.getName();

        Set<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        User authenticatedUser = (User)authentication.getPrincipal();
        User user = userRepository.findById(authenticatedUser.getId())
                .orElse(null);

        if(user == null){
            return false;
        }

        return roles.contains("ROLE_ADMIN") || loggedInUsername.equals(user.getUsername());
    }

    public boolean canAddUser(Authentication authentication){
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()).contains("ROLE_ADMIN");
    }

    public boolean canDeleteUser(Authentication authentication){
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()).contains("ROLE_ADMIN");
    }

}
