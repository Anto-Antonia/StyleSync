package com.example.StyleSync.controller;

import com.example.StyleSync.dto.request.role.RoleRequest;
import com.example.StyleSync.dto.request.user.UserRequest;
import com.example.StyleSync.dto.request.user.UserUpdateUsername;
import com.example.StyleSync.dto.response.product.FavoriteProductResponse;
import com.example.StyleSync.dto.response.role.RoleResponse;
import com.example.StyleSync.dto.response.user.UserResponse;
import com.example.StyleSync.entity.Role;
import com.example.StyleSync.service.UserRoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userRequest){
        UserResponse response = service.addUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id){
        UserResponse response = service.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getUserProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserResponse response = service.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<UserResponse> responses = service.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PatchMapping("/updateUsername")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updateUserUsername(@RequestBody @Valid UserUpdateUsername userUpdateUsername ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        service.updateUserUsername(email, userUpdateUsername);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{userId}/addToFav/{itemId}")
    public ResponseEntity<String> addItemToFavorite(@PathVariable Integer userId, @PathVariable Integer itemId){
        service.addItemToFavorite(userId, itemId);
        return ResponseEntity.ok("Item added to favorites");
    }

    @DeleteMapping("/{userId}/removeItem/{itemId}")
    public ResponseEntity<String> removeItemFromFavorite(@PathVariable Integer userId, @PathVariable Integer itemId){
        service.removeItemFromFavorite(userId, itemId);
        return ResponseEntity.ok("Item removed from favorites");
    }

    @GetMapping("/getFavProducts/{userId}")
    public ResponseEntity<List<FavoriteProductResponse>> getFavoriteProducts(@PathVariable Integer userId){
        List<FavoriteProductResponse> responses = service.getFavoriteProducts(userId);
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId){
        service.deleteUser(userId);
        return ResponseEntity.ok("User with the id: " + userId + " has been deleted");
    }

    @PostMapping("/role/addRole")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> addRole(@RequestBody RoleRequest request){
        Role role = service.addRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @PostMapping("/role/addRoleToUser/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addRoleToUser(@PathVariable @Valid Integer userId, @RequestBody RoleRequest request){
        service.addRoleToUser(userId, request.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/role/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<RoleResponse> getRole(@PathVariable Integer id){
        RoleResponse response = service.getRole(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/role/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteRole(@PathVariable @Valid Integer id){
        service.deleteRole(id);
        return ResponseEntity.ok("The role with id " + id + " has been deleted");
    }
}
