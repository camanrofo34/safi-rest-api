package unv.upb.safi.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import unv.upb.safi.repository.RoleRepository;
import unv.upb.safi.repository.UserRepository;
import unv.upb.safi.service.impl.UserServiceImpl;

import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@EnableWebMvc
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /**
     * Constructor que inyecta el filtro JWT.
     *
     * @param jwtFilter Filtro JWT para la autenticación basada en tokens.
     */
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     * <p>
     * - Deshabilita CSRF (porque usamos autenticación JWT). <br>
     * - Configura CORS para permitir peticiones del frontend. <br>
     * - Define qué endpoints son públicos y cuáles requieren autenticación. <br>
     * - Establece la política de sesiones en STATELESS. <br>
     * - Agrega el filtro JWT antes de la autenticación estándar de Spring Security.
     * </p>
     *
     * @param http Objeto {@link HttpSecurity} para configurar la seguridad.
     * @return Configuración de seguridad aplicada.
     * @throws Exception Si ocurre algún error en la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilita CORS
                .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF (innecesario con JWT)
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("").permitAll() // Endpoints públicos
                        .anyRequest().permitAll() // Todo lo demás requiere autenticación
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin estado
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Agregar filtro JWT

        return http.build();
    }

    /**
     * Configura el `AuthenticationManager`, que maneja la autenticación en Spring Security.
     *
     * @param authenticationConfiguration Configuración de autenticación de Spring.
     * @return Objeto `AuthenticationManager` configurado.
     * @throws Exception Si ocurre un error al obtener el `AuthenticationManager`.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        return new UserServiceImpl(userRepository, roleRepository);
    }



    /**
     * Configura un proveedor de autenticación basado en DAO.
     * <p>
     * Se encarga de cargar los detalles del usuario desde la base de datos y
     * validar las credenciales utilizando un `PasswordEncoder`.
     * </p>
     *
     * @param userDetailsService Servicio para cargar los detalles del usuario.
     * @param passwordEncoder    Codificador de contraseñas.
     * @return Proveedor de autenticación configurado.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            @Autowired UserDetailsService userDetailsService,
            @Autowired PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }


    /**
     * Define el `PasswordEncoder` que se utilizará para codificar y verificar contraseñas.
     * <p>
     * Se usa `BCryptPasswordEncoder`, que es seguro y ampliamente utilizado.
     * </p>
     *
     * @return Instancia de `BCryptPasswordEncoder`.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    /**
     * Configura CORS (Cross-Origin Resource Sharing) para permitir peticiones del frontend Angular.
     * <p>
     * - Permite peticiones desde `<a href="http://localhost:4200">...</a>`. <br>
     * - Habilita métodos HTTP como `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS` y `PATCH`. <br>
     * - Permite las cabeceras `Authorization` y `Content-Type`. <br>
     * - Habilita el uso de credenciales (como tokens JWT).
     * </p>
     *
     * @return Configuración de CORS aplicada a todas las rutas (`/**`).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200",
                "http://speedorz.bucaramanga.upb.edu.co")); // Permitir frontend de Angular
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Métodos HTTP permitidos
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Cabeceras permitidas
        configuration.setAllowCredentials(true); // Permitir credenciales como JWT

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

