package cl.randstad.common.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import cl.randstad.common.exception.JwtValidationException;
import cl.randstad.common.helper.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtTokenService {

	private Key secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(Constants.SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    
    public String generateToken(UUID userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Constants.ACCESS_TOKEN_VALIDITY_SECONDS * 1000);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("roles", List.of("ROLE_USER", "ROLE_ADMIN")) // puedes ajustar dinámicamente si lo deseas
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
    	boolean isOK = false;
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            isOK = true;
        } catch (ExpiredJwtException ex) {
        	throw new JwtValidationException("El token ha expirado", ex);
        } catch (MalformedJwtException ex) {
            throw new JwtValidationException("El token está mal formado", ex);
        } catch (JwtException ex) {
            throw new JwtValidationException("El token JWT no es válido", ex);
        } catch (Exception ex) {
            throw new JwtValidationException("Error inesperado al validar el token", ex);
        }
        return isOK;
    }

    public UUID getUserIdFromToken(String token) {
        return UUID.fromString(getClaims(token).getSubject());
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            Claims claims = getClaims(token);
            String userId = claims.getSubject();

            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);

            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(userId, null, authorities);

        } catch (JwtException e) {
            System.out.println("Token inválido: " + e.getMessage());
            return null;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
