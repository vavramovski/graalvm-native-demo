package com.example.demo.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.nativex.hint.ResourceHint;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.Certificate;
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

    public JwtUtils(@Value("classpath:server.jks") Resource jksResource) throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(jksResource.getInputStream(), "jkspassword".toCharArray());
        Key key = keystore.getKey("jwtkey1", "jkspassword".toCharArray());
        Certificate cert = keystore.getCertificate("jwtkey1");
        PublicKey publicKey = cert.getPublicKey();

        this.publicKey = (PublicKey) publicKey;
        this.privateKey = (PrivateKey) key;
    }

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.PS512, privateKey)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(authToken);
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

