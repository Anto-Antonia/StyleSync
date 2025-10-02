package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.role.RoleRequest;
import com.example.StyleSync.dto.request.user.UserRequest;
import com.example.StyleSync.dto.request.user.UserUpdateUsername;
import com.example.StyleSync.dto.response.product.FavoriteProductResponse;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements UserRoleService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final UserRoleMapper mapper;

    public UserRoleServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ProductRepository productRepository, UserRoleMapper mapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public UserResponse addUser(UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findUserByEmail(userRequest.getEmail());
        if(existingUser.isPresent()){
            throw new EmailAlreadyExistsException("Email already in use: " + userRequest.getEmail());
        }

        User user = mapper.fromUserRequest(userRequest);

        Role role = roleRepository.findRoleByName("ROLE_USER")
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

        return user.stream()
                .map(mapper::fromUserResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found."));

        return new UserResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    @Override
    public void updateUserUsername(String email, UserUpdateUsername userUpdateUsername) {
        Optional<User> userOpt = userRepository.findUserByEmail(email);

        if(userOpt.isPresent()){
            User user1 = userOpt.get();
            user1.setFirstName(userUpdateUsername.getFirstName());
            user1.setLastName(userUpdateUsername.getLastName());
            userRepository.save(user1);
        } else{
            throw new UserNotFoundException("User not found.");
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
    public List<FavoriteProductResponse> getFavoriteProducts(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            List<Product> favorites = user.getFavoriteProducts();

            return favorites.stream()
                    .map(p -> new FavoriteProductResponse(p.getProductName(), p.getPrice(), p.getQuantity()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
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

            return  mapper.fromRoleResponse(role);
        } else{
        throw new RoleNotFoundException("Role not found.");
        }
    }

    @Override
    public void deleteRole(Integer id) {
    roleRepository.deleteById(id);
    }
}
