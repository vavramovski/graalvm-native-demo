package com.example.demo.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.token.validity}")
    private int jwtExpirationMs;

    @Value("${jwt.secret}")
    private String jwtSecret;

    PublicKey publicKey;
    PrivateKey privateKey;

    public JwtUtils() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair kp = keyGenerator.genKeyPair();
        publicKey = (PublicKey) kp.getPublic();
        privateKey = (PrivateKey) kp.getPrivate();
    }

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {

            Jwts.parser().setSigningKey(privateKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            System.out.printf("Invalid JWT signature: %s%n", e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.printf("Invalid JWT token: %s%n", e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.printf("JWT token is expired: %s%n", e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.printf("JWT token is unsupported: %s%n", e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.printf("JWT claims string is empty: %s%n", e.getMessage());
        }

        return false;
    }
}

