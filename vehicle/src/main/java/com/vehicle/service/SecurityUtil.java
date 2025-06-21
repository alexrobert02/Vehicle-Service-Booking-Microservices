package com.vehicle.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SecurityUtil {

    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {
            // JWT token - extract from token claims
            Map<String, Object> claims = jwt.getClaims();

            if (claims.containsKey("sub")) {
                return String.valueOf(claims.get("sub"));
            }
        }

        // fallback or no user_id claim found
        return null;
    }
}
