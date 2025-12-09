package ru.vladislavkomkov.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JWTUtils {
    private static final Map<String, SecretKey> KEY_CACHE = new ConcurrentHashMap<>();

    private static final long DEFAULT_EXPIRATION_TIME = 3600000L;

    private static SecretKey defaultSecretKey = generateSecretKey();

    public static SecretKey generateSecretKey(SignatureAlgorithm algorithm) {
        String algorithmName = algorithm.getValue();

        return KEY_CACHE.computeIfAbsent(algorithmName,
                k -> Keys.secretKeyFor(algorithm));
    }

    public static SecretKey generateSecretKey() {
        return generateSecretKey(SignatureAlgorithm.HS256);
    }

    public static String generateToken(Map<String, Object> claims,
                                       SecretKey secretKey,
                                       long expirationTime) {

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public static String generateToken(Map<String, Object> claims) {
        return generateToken(claims, defaultSecretKey);
    }

    public static String generateToken(Map<String, Object> claims, SecretKey secretKey) {
        return generateToken(claims, secretKey, DEFAULT_EXPIRATION_TIME);
    }

    public static String generateToken(String subject,
                                       SecretKey secretKey,
                                       long expirationTime) {

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public static Claims extractAllClaims(String token, SecretKey secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String extractClaim(String token,
                                     String claimKey) {

        return extractClaim(token, claimKey, String.class);
    }

    public static <T> T extractClaim(String token,
                                     String claimKey,
                                     Class<T> clazz) {

        return extractClaim(token, defaultSecretKey, claimKey, clazz);
    }


    public static <T> T extractClaim(String token,
                                     SecretKey secretKey,
                                     String claimKey,
                                     Class<T> clazz) {

        Claims claims = extractAllClaims(token, secretKey);
        return claims.get(claimKey, clazz);
    }

    public static String extractSubject(String token, SecretKey secretKey) {
        return extractAllClaims(token, secretKey).getSubject();
    }

    public static boolean isTokenValid(String token, SecretKey secretKey) {
        try {
            extractAllClaims(token, secretKey);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isTokenExpired(String token, SecretKey secretKey) {
        try {
            Claims claims = extractAllClaims(token, secretKey);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public static Date getExpirationDate(String token, SecretKey secretKey) {
        Claims claims = extractAllClaims(token, secretKey);
        return claims.getExpiration();
    }

    public static Date getIssuedAtDate(String token, SecretKey secretKey) {
        Claims claims = extractAllClaims(token, secretKey);
        return claims.getIssuedAt();
    }
}