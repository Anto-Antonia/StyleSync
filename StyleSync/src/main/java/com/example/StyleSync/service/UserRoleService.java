package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.role.RoleRequest;
import com.example.StyleSync.dto.request.user.UserRequest;
import com.example.StyleSync.dto.request.user.UserUpdateUsername;
import com.example.StyleSync.dto.response.product.FavoriteProductResponse;
import com.example.StyleSync.dto.response.role.RoleResponse;
import com.example.StyleSync.dto.response.user.UserResponse;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.entity.Role;

import java.util.List;

public interface UserRoleService {
    UserResponse addUser(UserRequest userRequest);
    UserResponse getUserById(Integer id);
    List<UserResponse> getAllUsers();
    UserResponse getUserByEmail(String email);
    void updateUserUsername(String email, UserUpdateUsername userUpdateUsername);
    void addItemToFavorite(String email, Integer itemId);
    void removeItemFromFavorite(String email, Integer itemId);
    List<FavoriteProductResponse> getFavoriteProducts(String email);
    void deleteUser(Integer id);
    Role addRole(RoleRequest request);
    void addRoleToUser(Integer userId, String name);
    RoleResponse getRole(Integer id);
    List<RoleResponse> getAllRoles();
    void deleteRole(Integer id);
}
