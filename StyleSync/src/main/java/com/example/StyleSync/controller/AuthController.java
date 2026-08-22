package com.example.StyleSync.controller;

import com.example.StyleSync.dto.request.user.RegisterRequest;
import com.example.StyleSync.dto.request.user.SignInRequest;
import com.example.StyleSync.dto.response.user.RegisterResponse;
import com.example.StyleSync.dto.response.user.SignInResponse;
import com.example.StyleSync.entity.RevokedToken;
import com.example.StyleSync.jwt.JwtUtils;
import com.example.StyleSync.repository.RevokedTokenRepository;
import com.example.StyleSync.service.security.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RevokedTokenRepository tokenRepository;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtUtils jwtUtils, RevokedTokenRepository tokenRepository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.tokenRepository = tokenRepository;
    }

    @PostMapping("/api/auth/signIn")
    public ResponseEntity<SignInResponse> signIn(@RequestBody @Valid SignInRequest signInRequest){
        SignInResponse response = authService.signIn(signInRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request){
        RegisterResponse response = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){
        String header = request.getHeader("Authorization");

        if(header != null && header.startsWith("Bearer ")){
            String token = header.substring(7);

            Date expirationDate = jwtUtils.getExpirationFromJwt(token);
            LocalDateTime expiresAt = expirationDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            tokenRepository.save(new RevokedToken(null, token, expiresAt));
        }

        return ResponseEntity.noContent().build();
    }
}
