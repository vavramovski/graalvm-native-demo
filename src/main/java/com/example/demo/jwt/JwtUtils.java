package com.example.demo.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.token.validity}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) throws JsonProcessingException {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Date expire = new Date();
        expire.setTime(expire.getTime() + jwtExpirationMs);
        JwtToken token = new JwtToken(userPrincipal.getUsername(), List.of("user"), expire.getTime());

        return token.toString();
    }

    public String getUserNameFromJwtToken(String token) throws JsonProcessingException, NoSuchAlgorithmException {
        return new JwtToken(token).getSubject();
    }

    public boolean validateJwtToken(String authToken) throws JsonProcessingException, NoSuchAlgorithmException {
        return new JwtToken(authToken).isValid();
    }

}
