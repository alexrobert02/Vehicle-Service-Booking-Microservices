package com.authentication.config;

import lombok.Builder;

@Builder
public class UserRepresentation {
    private String username;
    private String password;
}
