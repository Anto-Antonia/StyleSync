package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.role.RoleRequest;
import com.example.StyleSync.dto.request.user.UserRequest;
import com.example.StyleSync.dto.request.user.UserUpdateUsername;
import com.example.StyleSync.dto.response.role.RoleResponse;
import com.example.StyleSync.dto.response.user.UserResponse;
import com.example.StyleSync.entity.Role;
import com.example.StyleSync.entity.User;
import com.example.StyleSync.exceptions.role.RoleNotFoundException;
import com.example.StyleSync.exceptions.user.EmailAlreadyExistsException;
import com.example.StyleSync.exceptions.user.UserNotFoundException;
import com.example.StyleSync.mapper.UserRoleMapper;
import com.example.StyleSync.repository.ProductRepository;
import com.example.StyleSync.repository.RoleRepository;
import com.example.StyleSync.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements UserRoleService{

    private static UserRepository userRepository;
    private static RoleRepository repository;
    private static ProductRepository productRepository;
    private static UserRoleMapper mapper;

    @Override
    public UserResponse addUser(UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findUserByEmail(userRequest.getEmail());
        if(existingUser.isPresent()){
            throw new EmailAlreadyExistsException("Email already in use: " + userRequest.getEmail());
        }

        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());

        Role role = repository.findRoleByName("user")
                .orElseThrow(()-> new RoleNotFoundException("Role 'user' not found in DB"));

        user.setRole(role);

        userRepository.save(user);

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                role.getName()
        );
    }

    @Override
    public UserResponse getUserById(Integer id) {
       User user = userRepository.findById(id)
               .orElseThrow(()-> new UserNotFoundException("User with id " + id + " not found."));

        return mapper.fromUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> user = userRepository.findAll();
        List<UserResponse> responses = user.stream()
                .map(element -> mapper.fromUserResponse(element)).collect(Collectors.toList());

        return responses;
    }

    @Override
    public void updateUserUsername(Integer id, UserUpdateUsername userUpdateUsername) {

    }

    @Override
    public void addItemToFavorite(Integer userId, Integer itemId) {

    }

    @Override
    public void deleteUser(Integer id) {

    }

    @Override
    public RoleResponse addRole(RoleRequest request) {
        return null;
    }

    @Override
    public RoleResponse getRole(Integer id) {
        return null;
    }

    @Override
    public void deleteRole(Integer id) {

    }
}
