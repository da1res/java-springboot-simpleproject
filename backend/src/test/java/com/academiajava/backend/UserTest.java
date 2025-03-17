package com.academiajava.backend;

import com.academiajava.backend.domain.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private Validator validator;

    @BeforeEach
    void setup(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUser(){
        User user = new User("Jose","ze1", LocalDateTime.now(), LocalDateTime.now(), "passWord", "ROLE_USER");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "User should be valid");
    }

    @Test
    void testInvalidUserNameTooShort(){
        User user = new User("Jo","ze1", LocalDateTime.now(), LocalDateTime.now(), "passWord", "ROLE_USER");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Name must have at least 3 characters");
    }

    @Test
    void testInvalidUserUsernameTooShort(){
        User user = new User("Jose","ze", LocalDateTime.now(), LocalDateTime.now(), "passWord", "ROLE_USER");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Username must have at least 3 characters");
    }

    @Test
    void testInvalidUsernamePattern(){
        User user = new User("Jose","ze_ze!", LocalDateTime.now(), LocalDateTime.now(), "passWord", "ROLE_USER");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Username must follow the pattern ^[a-zA-Z0-9]+$");
    }

}
