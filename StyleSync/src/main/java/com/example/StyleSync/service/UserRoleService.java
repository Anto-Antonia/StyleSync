package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.role.RoleRequest;
import com.example.StyleSync.dto.request.user.UserRequest;
import com.example.StyleSync.dto.request.user.UserUpdateUsername;
import com.example.StyleSync.dto.response.role.RoleResponse;
import com.example.StyleSync.dto.response.user.UserResponse;

import java.util.List;

public interface UserRoleService {
    UserResponse addUser(UserRequest userRequest);
    UserResponse getUserById(Integer id);
    List<UserResponse> getAllUsers();
    void updateUserUsername(Integer id, UserUpdateUsername userUpdateUsername);
    void addItemToFavorite(Integer userId, Integer itemId);
    void deleteUser(Integer id);
    RoleResponse addRole(RoleRequest request);
    RoleResponse getRole(Integer id);
    void deleteRole(Integer id);
}
