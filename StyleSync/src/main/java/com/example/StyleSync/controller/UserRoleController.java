package com.example.StyleSync.controller;

import com.example.StyleSync.dto.request.user.UserRequest;
import com.example.StyleSync.dto.request.user.UserUpdateUsername;
import com.example.StyleSync.dto.response.user.UserResponse;
import com.example.StyleSync.service.UserRoleService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserRoleController {
    private final UserRoleService service;

    public UserRoleController(UserRoleService service) {
        this.service = service;
    }

    @PostMapping("/addUser")
    @PreAuthorize("hasRol('admin')")
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userRequest){
        UserResponse response = service.addUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id){
        UserResponse response = service.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<UserResponse> responses = service.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateUserUsername(@PathVariable Integer id, @RequestBody @Valid UserUpdateUsername userUpdateUsername ){
        service.updateUserUsername(id, userUpdateUsername);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
