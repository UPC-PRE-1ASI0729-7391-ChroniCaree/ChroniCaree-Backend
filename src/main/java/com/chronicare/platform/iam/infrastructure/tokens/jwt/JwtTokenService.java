package com.chronicare.platform.iam.infrastructure.tokens.jwt;

import com.chronicare.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.chronicare.platform.iam.domain.model.aggregates.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtTokenService implements TokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenService.class);

    @Value("${authorization.jwt.secret}")
    private String secret;

    @Value("${authorization.jwt.expiration.days}")
    private int expirationDays;

    @Override
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        return generateToken(username);
    }

    @Override
    public String generateToken(String username) {
        return generateToken(username, null);
    }

    @Override
    public String generateToken(String username, String role) {
        return generateTokenWithClaims(username, role, null, null, null);
    }

    /**
     * Generate token with full user claims for Hospital Admin API
     */
    public String generateTokenWithClaims(String email, String role, Long userId, String name, Long tenantId) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiration = DateUtils.addDays(now, expirationDays);
        
        // El subject (sub) debe ser String, usamos el userId como subject
        String subject = userId != null ? String.valueOf(userId) : email;
        
        var builder = Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key);
        
        if (role != null) {
            builder.claim("role", role.toLowerCase());
        }
        if (userId != null) {
            builder.claim("userId", userId);
        }
        if (name != null) {
            builder.claim("name", name);
        }
        if (tenantId != null) {
            builder.claim("tenantId", tenantId);
        }
        builder.claim("email", email);
        
        return builder.compact();
    }

    /**
     * Generate token from User entity with all claims
     */
    public String generateTokenFromUser(User user) {
        return generateTokenWithClaims(
            user.getEmailAddress(),
            user.getRole().getName(),
            user.getId(),
            user.getName(),
            user.getTenantId()
        );
    }

    @Override
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public String getUsernameFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        // Retornar el email desde el claim, ya que el subject es el userId
        String email = claims.get("email", String.class);
        return email != null ? email : claims.getSubject();
    }

    public String getBearerTokenFrom(HttpServletRequest token) {
        String bearerToken = token.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
