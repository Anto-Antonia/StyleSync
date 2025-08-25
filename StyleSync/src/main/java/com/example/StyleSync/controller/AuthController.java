package com.example.StyleSync.controller;

import com.example.StyleSync.dto.request.user.RegisterRequest;
import com.example.StyleSync.dto.request.user.SignInRequest;
import com.example.StyleSync.dto.response.user.RegisterResponse;
import com.example.StyleSync.dto.response.user.SignInResponse;
import com.example.StyleSync.service.security.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
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
}
