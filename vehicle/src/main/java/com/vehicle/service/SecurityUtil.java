package com.vehicle.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SecurityUtil {

    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {
            Map<String, Object> claims = jwt.getClaims();

            if (claims.containsKey("sub")) {
                return String.valueOf(claims.get("sub"));
            }
        }

        return null;
    }
}
