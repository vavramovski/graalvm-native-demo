package com.example.demo.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.RsaProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Date;


@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;


    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtUtils() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(4096);
        // PS512 requires 4096 key size
        KeyPair kp = kpg.generateKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
    }

    private static final int jwtExpirationMs = 60 * 60 * 1000;


    // sign with private key
    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.PS512, privateKey)
                .compact();
    }

    // decomposing using public key
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody().getSubject();
    }

    // decomposing using public key
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(authToken);
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

