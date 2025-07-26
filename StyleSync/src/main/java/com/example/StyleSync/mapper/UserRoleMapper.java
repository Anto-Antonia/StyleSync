package com.example.StyleSync.mapper;

import com.example.StyleSync.dto.request.role.RoleRequest;
import com.example.StyleSync.dto.request.user.UserRequest;
import com.example.StyleSync.dto.response.role.RoleResponse;
import com.example.StyleSync.dto.response.user.UserResponse;
import com.example.StyleSync.entity.Role;
import com.example.StyleSync.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserRoleMapper {
   // private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // for when working with the registrations and all

    public User fromUserRequest(UserRequest userRequest){
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        //user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        return user;
    }

    public UserResponse fromUserResponse(User user){
       String roleName = user.getRole() !=null ? user.getRole().getName() : "Unknown";

       return new UserResponse(
               user.getId(),
               user.getFirstName(),
               user.getLastName(),
               user.getEmail(),
               roleName
       );
    }

    public Role fromRoleRequest(RoleRequest request){
        Role role = new Role();

        role.setName(request.getName());
        role.setUsers(new ArrayList<>());

        return role;
    }

    public RoleResponse fromRoleResponse(Role role){
        RoleResponse response =new RoleResponse();

        response.setRoleName(role.getName());

        return response;
    }
}
