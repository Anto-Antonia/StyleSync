package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.role.RoleRequest;
import com.example.StyleSync.dto.request.user.UserRequest;
import com.example.StyleSync.dto.request.user.UserUpdateUsername;
import com.example.StyleSync.dto.response.role.RoleResponse;
import com.example.StyleSync.dto.response.user.UserResponse;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.entity.Role;
import com.example.StyleSync.entity.User;
import com.example.StyleSync.exceptions.product.ProductNotFoundException;
import com.example.StyleSync.exceptions.role.RoleNotFoundException;
import com.example.StyleSync.exceptions.user.EmailAlreadyExistsException;
import com.example.StyleSync.exceptions.user.UserNotFoundException;
import com.example.StyleSync.mapper.UserRoleMapper;
import com.example.StyleSync.repository.ProductRepository;
import com.example.StyleSync.repository.RoleRepository;
import com.example.StyleSync.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements UserRoleService{

    private static UserRepository userRepository;
    private static RoleRepository roleRepository;
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

        Role role = roleRepository.findRoleByName("user")
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
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setFirstName(userUpdateUsername.getFirstname());
            user.setLastName(userUpdateUsername.getLastname());
            userRepository.save(user);
        } else{
            throw new UserNotFoundException("The user with id: " + id + " was not found");
        }
    }

    @Override
    public void addItemToFavorite(Integer userId, Integer itemId) {
    User user = userRepository.findById(userId)
            .orElseThrow(()-> new UserNotFoundException("User not found!"));
    Product product = productRepository.findById(itemId)
            .orElseThrow(()-> new ProductNotFoundException("Product not found."));

    if(!user.getFavoriteProducts().contains(product)){
        user.getFavoriteProducts().add(product);
        userRepository.save(user);
        }
    }

    @Transactional
    @Override
    public void removeItemFromFavorite(Integer userId, Integer itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found!"));
        Product product = productRepository.findById(itemId)
                .orElseThrow(()-> new ProductNotFoundException("Product not found."));

        if(!user.getFavoriteProducts().contains(product)){
            user.getFavoriteProducts().remove(product);
            userRepository.save(user);
        }
    }


    @Override
    public void deleteUser(Integer id) {
    userRepository.deleteById(id);
    }

    @Override
    public Role addRole(RoleRequest request) {
        Role role = mapper.fromRoleRequest(request);
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(Integer userId, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found!"));
        Role role =  roleRepository.findRoleByName(name)
                .orElseThrow(()-> new RoleNotFoundException("Role with name: " + name + ", not found"));


        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public RoleResponse getRole(Integer id) {
        Optional<Role> roleOptional = roleRepository.findById(id);

        if(roleOptional.isPresent()){
            Role role = roleOptional.get();
            RoleResponse response = mapper.fromRoleResponse(role);

            return  response;
        }else{
        throw new RoleNotFoundException("Role not found.");
        }
    }

    @Override
    public void deleteRole(Integer id) {
    roleRepository.deleteById(id);
    }
}
