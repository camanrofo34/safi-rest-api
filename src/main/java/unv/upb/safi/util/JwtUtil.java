package unv.upb.safi.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY;
    private final long EXPIRATION_TIME;

    /**
     * Constructor que inicializa la clave secreta y el tiempo de expiración.
     *
     * @param base64Secret Clave secreta en formato Base64, definida en `application.properties`.
     * @param expirationTime Tiempo de expiración del token en milisegundos.
     */
    public JwtUtil(@Value("${jwt.secret}") String base64Secret,
                   @Value("${jwt.expiration}") long expirationTime) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Secret));
        this.EXPIRATION_TIME = expirationTime;
    }

    /**
     * Genera un token JWT a partir del nombre de usuario.
     *
     * @param username Nombre de usuario autenticado.
     * @return Token JWT generado.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date()) // Fecha de emisión del token
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Fecha de expiración
                .signWith(SECRET_KEY) // Firma con la clave secreta
                .compact();
    }

    /**
     * Valida la autenticidad de un token JWT.
     *
     * @param token Token JWT recibido.
     * @return true si el token es válido, false si es inválido o expirado.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token inválido o expirado
        }
    }

    /**
     * Extrae el nombre de usuario desde un token JWT válido.
     *
     * @param token Token JWT del cual se extraerá el nombre de usuario.
     * @return Nombre de usuario contenido en el token.
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Obtiene el token JWT desde el encabezado "Authorization" de una petición HTTP.
     *
     * @param request Petición HTTP recibida.
     * @return Token JWT si está presente, de lo contrario, null.
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;
    }
}