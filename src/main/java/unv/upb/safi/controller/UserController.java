package unv.upb.safi.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import unv.upb.safi.domain.dto.request.LoginRequest;
import unv.upb.safi.service.UserService;
import unv.upb.safi.util.JwtUtil;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        UUID transactionId = UUID.randomUUID();
        MDC.put("transactionId", transactionId.toString());

        log.info("Transaction ID: {}, User {} is trying to login", transactionId, loginRequest.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            log.info("Transaction ID: {}, User {} logged in successfully", transactionId, loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }finally {
            MDC.clear();
        }
    }

    @GetMapping("/verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        UUID transactionId = UUID.randomUUID();
        MDC.put("transactionId", transactionId.toString());
        log.info("Transaction ID: {}, Sending verification code to {}", transactionId, email);

        try {
            userService.sendVerificationCode(email);
            log.info("Transaction ID: {}, Verification code sent to {}", transactionId, email);
            return ResponseEntity.status(HttpStatus.OK).body("Verification code sent to " + email);
        }finally {
            MDC.clear();
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String email,
                                            @RequestParam String newPassword,
                                            @RequestParam String verificationCode) {

        UUID transactionId = UUID.randomUUID();
        MDC.put("transactionId", transactionId.toString());

        log.info("Transaction ID: {}, Changing password for {}", transactionId, email);

        try {
            userService.changePassword(email, newPassword, verificationCode);
            log.info("Transaction ID: {}, Password changed successfully for {}", transactionId, email);
            return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
        }finally {
            MDC.clear();
        }
    }
}
