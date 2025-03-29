package unv.upb.safi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unv.upb.safi.domain.dto.request.LoginRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Aquí puedes generar un token JWT y devolverlo en la respuesta
        // Por simplicidad, solo devolvemos un mensaje de éxito
        return ResponseEntity.ok("Login exitoso");
    }
}
