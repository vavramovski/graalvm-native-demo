package com.example.demo.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final int jwtExpirationMs = 60 * 60 * 1000;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            System.err.println("Invalid JWT signature: {}");
        } catch (MalformedJwtException e) {
            System.err.println("Invalid JWT token: {}");
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token is expired: {}");
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT token is unsupported: {}");
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: {}");
        }
        return false;
    }


}

