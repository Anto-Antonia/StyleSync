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

    @PostMapping("/addToFav/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addItemToFavorite( @PathVariable Integer itemId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        service.addItemToFavorite(email, itemId);
        return ResponseEntity.ok("Item added to favorites");
    }

    @DeleteMapping("/removeItem/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> removeItemFromFavorite( @PathVariable Integer itemId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        service.removeItemFromFavorite(email, itemId);
        return ResponseEntity.ok("Item removed from favorites successfully.");
    }

    @GetMapping("/getFavProducts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FavoriteProductResponse>> getFavoriteProducts(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<FavoriteProductResponse> responses = service.getFavoriteProducts(email);
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponse> getRole(@PathVariable Integer id){
        RoleResponse response = service.getRole(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleResponse>> getAllRoles(){
        List<RoleResponse> response = service.getAllRoles();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping("/role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteRole(@PathVariable @Valid Integer id){
        service.deleteRole(id);
        return ResponseEntity.ok("The role with id " + id + " has been deleted");
    }
}
