package com.chronicare.platform.iam.application.internal.outboundservices.tokens;

import org.springframework.security.core.Authentication;

public interface TokenService {
    String generateToken(Authentication authentication);
    String generateToken(String username);
    String generateToken(String username, String role);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
}
