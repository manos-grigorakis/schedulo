package com.mgrigorakis.schedulo.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Component
public class JwtService {
    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.security.jwt.expiration-in-ms}")
    private Long jwtExpirationInMs;

    public String generateToken(Long id, String role) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("role", role);

        return createToken(id, claims);
    }

    public Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
    }

    private String createToken(Long id, Map<String, Object> claims) {
        Date tokenExpirationDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);

        return Jwts.builder()
                .claims(claims)
                .subject(id.toString())
                .issuedAt(new Date())
                .expiration(tokenExpirationDate)
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
