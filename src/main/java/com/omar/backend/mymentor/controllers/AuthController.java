package com.omar.backend.mymentor.controllers;

import com.omar.backend.mymentor.auth.TokenJwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "${allowed.origins}")
@Tag(name = "Auth", description = "API para gestionar la autenticación")
public class AuthController {

    @Value("${session.expiration}")
    private int expiration;

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith(TokenJwtConfig.PREFIX_TOKEN)) {
            token = token.replace(TokenJwtConfig.PREFIX_TOKEN, "");
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(TokenJwtConfig.SECRET_KEY)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();
                
                String newToken = Jwts.builder()
                        .setClaims(claims)
                        .setSubject(email)
                        .signWith(TokenJwtConfig.SECRET_KEY)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + expiration))
                        .compact();

                Map<String, Object> response = new HashMap<>();
                response.put("token", newToken);
                response.put("message", "Token renovado con éxito");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Token inválido o expirado");
            }
        }
        return ResponseEntity.badRequest().body("Token no proporcionado");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body("Sesión cerrada exitosamente");
    }
}