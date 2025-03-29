package unv.upb.safi.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import unv.upb.safi.service.impl.UserServiceImpl;
import unv.upb.safi.util.JwtUtil;

import java.io.IOException;

/**
 * Filtro de autenticación JWT para procesar cada solicitud HTTP y validar el token JWT.
 * <p>
 * Este filtro intercepta cada solicitud entrante, verifica si contiene un token JWT
 * válido en el encabezado "Authorization" y, si es válido, establece la autenticación en el contexto de seguridad.
 * </p>
 *
 * <p>Excluye ciertas rutas del filtrado para permitir el acceso sin autenticación.</p>
 *
 * @author Camilo
 * @version 1.0
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * Constructor que inyecta los servicios necesarios.
     *
     * @param userDetailsService Servicio para cargar detalles del usuario desde la base de datos.
     * @param jwtUtil Utilidad para gestionar la generación y validación de tokens JWT.
     */
    @Autowired
    public JwtFilter(UserServiceImpl userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Filtra cada solicitud entrante y valida la autenticación mediante JWT.
     * <p>
     * Si la solicitud se dirige a rutas excluidas (como autenticación o promociones), se omite el filtrado.
     * Si la solicitud contiene un token JWT válido, se extrae el usuario y se establece en el contexto de seguridad.
     * </p>
     *
     * @param request  Solicitud HTTP entrante.
     * @param response Respuesta HTTP.
     * @param chain    Cadena de filtros para continuar la ejecución.
     * @throws ServletException Si ocurre un error durante el filtrado.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestPath = request.getServletPath();

        // Excluir de la autenticación las rutas de autenticación
        if (requestPath.startsWith("/")) {
            chain.doFilter(request, response);
            return;
        }

        // Obtener el encabezado Authorization de la solicitud
        String authorizationHeader = request.getHeader("Authorization");

        // Si no hay token o el formato es incorrecto, continuar sin autenticación
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Extraer el token JWT del encabezado
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        // Si el usuario es válido y no está autenticado en el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validar el token y establecer autenticación
            if (jwtUtil.validateToken(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continuar con la ejecución de la solicitud
        chain.doFilter(request, response);
    }
}
