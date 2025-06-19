package com.example.pedidosYA.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

    private static final long EXPIRATION = Long.parseLong(
            System.getenv().getOrDefault("JWT_EXP", "3600000")); // 1 hora por defecto
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class JwtUtil {
    private static final long EXPIRATION = 3600000L; // 1 hora

    // Usá un valor fijo o sacalo de env si lo configuraste bien (ver más abajo)
    private static final String BASE64_SECRET = "LhQx5N2R+IoN9T7VxM5B0rJL4GyXZWdt9h5C5eT4EyI=";

    // Generamos la clave desde el base64
    private static final SecretKey SECRET_KEY = new SecretKeySpec(
            Base64.getDecoder().decode(BASE64_SECRET),
            SignatureAlgorithm.HS256.getJcaName()
    );

    public static String createToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("JWT expirado: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("JWT inválido: " + e.getMessage());
        }
        return false;
    }

    public static String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @SuppressWarnings("unchecked")
    public static List<String> getRoles(String token) {
        return (List<String>) Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("roles");
    }
}
