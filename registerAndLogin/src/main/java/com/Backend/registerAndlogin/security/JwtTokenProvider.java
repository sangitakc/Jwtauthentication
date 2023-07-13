package com.Backend.registerAndlogin.security;

import com.Backend.registerAndlogin.repository.UserTokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;


@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;
    private final UserTokenRepo userTokenRepo;

    public JwtTokenProvider(UserTokenRepo userTokenRepo) {
        this.userTokenRepo = userTokenRepo;
    }


    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    public Authentication getAuthentication(String token) {
        String username = getUsernameFromToken(token);
        return new UsernamePasswordAuthenticationToken(username, null, null);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
//            Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(jwtSecret.getBytes())).parseClaimsJws(token);
            Boolean exist= userTokenRepo.existsByToken(token);
            return exist;
        } catch (Exception ex) {
            return false;
        }
    }
}
