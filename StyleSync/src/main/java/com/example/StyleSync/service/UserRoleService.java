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
    void updateUserUsername(Integer id, UserUpdateUsername userUpdateUsername);
    void addItemToFavorite(Integer userId, Integer itemId);
    void removeItemFromFavorite(Integer userId, Integer itemId);
    List<FavoriteProductResponse> getFavoriteProducts(Integer userId);
    void deleteUser(Integer id);
    Role addRole(RoleRequest request);
    void addRoleToUser(Integer userId, String name);
    RoleResponse getRole(Integer id);
    void deleteRole(Integer id);
}
