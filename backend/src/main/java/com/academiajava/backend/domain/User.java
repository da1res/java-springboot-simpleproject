package com.academiajava.backend.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor
@ToString @AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails{

    @Id
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 100, message 
    = "Name must be between 1 and 100 characters")
    private String name;

    @NotNull(message = "Username cannot be null")
    @Size(min = 3, max = 50, message 
      = "Username must be between 1 and 50 characters")
    @Pattern(regexp="^[a-zA-Z0-9]+$")
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime creation;
    private LocalDateTime lastUpdated;

    private String password;

    @Version
    private Long version;

    public User(String name, String username, LocalDateTime createTimestamp, LocalDateTime updateTimestamp, String password, String role){
        this.name = name;
        this.username = username;
        this.creation = createTimestamp;
        this.lastUpdated = updateTimestamp;
        this.password = password;
        this.role = Role.valueOf(role);
      }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority(role.name()));
    }


}
