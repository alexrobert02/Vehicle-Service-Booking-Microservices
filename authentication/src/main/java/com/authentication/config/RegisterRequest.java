package com.authentication.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    //@NotBlank(message = "Username is required")
    //@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    //@NotBlank(message = "Password is required")
    private String password;

    private Role role;

    //@NotNull(message = "Role is required")
    //private Role role;
}
