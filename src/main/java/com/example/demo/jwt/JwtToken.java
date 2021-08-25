package com.example.demo.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author user
 */
public class JwtToken {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${jwt.secret}")
    private String SECRET_KEY;


    private static final String ISSUER = "mason.metamug.net";
    private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private HashMap<String, Object> payload = new HashMap<>();
    private String signature;
    private String encodedHeader;

    private JwtToken() throws JsonProcessingException {
        HashMap<String, Object> header = objectMapper.readValue(JWT_HEADER, HashMap.class);
        encodedHeader = encode(header);
        SECRET_KEY = "liki";
    }

    public JwtToken(HashMap<String, Object> payload) throws JsonProcessingException {
        this((String) payload.get("sub"), (List<String>) payload.get("aud"), (Long) payload.get("exp"));
    }

    public JwtToken(String sub, List<String> aud, long expires) throws JsonProcessingException {
        this();
        payload.put("sub", sub);
        payload.put("aud", aud);
        payload.put("exp", expires);
        payload.put("iat", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        payload.put("iss", ISSUER);
        payload.put("jti", UUID.randomUUID().toString()); //how do we use this?
        signature = hmacSha256(encodedHeader + "." + encode(payload), SECRET_KEY);
    }

    /**
     * For verification
     *
     * @param token
     * @throws java.security.NoSuchAlgorithmException
     */
    public JwtToken(String token) throws NoSuchAlgorithmException, JsonProcessingException {
        this();
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid Token format");
        }
        if (encodedHeader.equals(parts[0])) {
            encodedHeader = parts[0];
        } else {
            throw new NoSuchAlgorithmException("JWT Header is Incorrect: " + parts[0]);
        }

//        payload = new JSONObject(decode(parts[1]));
        payload = objectMapper.readValue(decode(parts[1]), HashMap.class);
        if (payload.isEmpty()) {
            throw new RuntimeException("Payload is Empty: ");
        }
        if (!payload.containsKey("exp")) {
            throw new RuntimeException("Payload doesn't contain expiry " + payload);
        }
        signature = parts[2];

        if (!isValid()) {
            throw new RuntimeException("Invalid Token - not signed by us");
        }
    }

    @Override
    public String toString() {
        try {
            return encodedHeader + "." + encode(payload) + "." + signature;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isValid() throws JsonProcessingException {
        return (Long) payload.get("exp") > (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) //token not expired
                && signature.equals(hmacSha256(encodedHeader + "." + encode(payload), SECRET_KEY)); //signature matched
    }

    public String getSubject() {
        return (String) payload.get("sub");
    }

    public List<String> getAudience() {
        List<Object> arr = (List<Object>) payload.get("aud");
        List<String> list = new ArrayList<>();
        return (List<String>) arr.stream().map(o -> o.toString());
    }

    private static String encode(HashMap<String, Object> obj) throws JsonProcessingException {

        return encode(objectMapper.writeValueAsString(obj).getBytes(StandardCharsets.UTF_8));
    }

    private static String encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    /**
     * Sign with HMAC SHA256 (HS256)
     *
     * @param data
     * @return
     * @throws Exception
     */
    private String hmacSha256(String data, String secret) {
        try {

            //MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = secret.getBytes(StandardCharsets.UTF_8);//digest.digest(secret.getBytes(StandardCharsets.UTF_8));

            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(hash, "HmacSHA256");
            sha256Hmac.init(secretKey);

            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return encode(signedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            Logger.getLogger(JwtToken.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }


//    public static void main(String[] args) throws JsonProcessingException {
//        Date expire = new Date();
//        expire.setTime(expire.getTime() + 30000);
//
//        JwtToken token = new JwtToken("viktor", List.of("aud1"), expire.getTime());
//        System.out.println(token);
//    }
}