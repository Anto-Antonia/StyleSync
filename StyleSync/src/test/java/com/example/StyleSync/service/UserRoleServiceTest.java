package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.user.UserRequest;
import com.example.StyleSync.dto.request.user.UserUpdateUsername;
import com.example.StyleSync.dto.response.product.ProductResponse;
import com.example.StyleSync.dto.response.role.RoleResponse;
import com.example.StyleSync.dto.response.user.UserResponse;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.entity.Role;
import com.example.StyleSync.entity.User;
import com.example.StyleSync.exceptions.role.RoleNotFoundException;
import com.example.StyleSync.exceptions.user.EmailAlreadyExistsException;
import com.example.StyleSync.exceptions.user.UserNotFoundException;
import com.example.StyleSync.mapper.ProductMapper;
import com.example.StyleSync.mapper.UserRoleMapper;
import com.example.StyleSync.repository.ProductRepository;
import com.example.StyleSync.repository.RoleRepository;
import com.example.StyleSync.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRoleMapper mapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private UserRoleServiceImpl service;

    private User user;
    private Role role;
    private Product product;
    private UserResponse userResponse;
    private RoleResponse roleResponse;

    @BeforeEach
    void setup(){
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        product = new Product();
        product.setId(1);
        product.setProductName("Skirt");

        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Wick");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setRole(role);
        user.setFavoriteProducts(new ArrayList<>(Arrays.asList(product)));
    }

    //any(User.class) instead of exact instance
    //  VERIFYING BEHAVIOR NOT IMPLEMENTATION
    @Test
    void addUser_whenSuccessful_returnUserResponse(){
        UserRequest request = new UserRequest("John", "Wick", "email@email.com", "password");

        //GIVEN
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(mapper.fromUserRequest(request)).thenReturn(user);

        when(roleRepository.findRoleByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //WHEN
       UserResponse response = service.addUser(request);

        //THEN
        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("Wick", response.getLastName());
        assertEquals("email@email.com", response.getEmail());
        assertEquals("ROLE_USER", response.getRole());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void addUser_whenEmailAlreadyExists_throwsException(){
        UserRequest request = new UserRequest();
        request.setEmail("email@email.com");

        //GIVEN
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(user));

        //WHEN
        assertThrows(EmailAlreadyExistsException.class, ()-> service.addUser(request));

        //THEN
        verify(userRepository, never()).save(any());  // prevent creating duplicate users
    }

    @Test
    void addUser_whenRoleNotFound_throwsException(){
        UserRequest request = new UserRequest();
        request.setEmail("email@email.com");

        //GIVEN
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(mapper.fromUserRequest(request)).thenReturn(user);
        when(roleRepository.findRoleByName("ROLE_USER")).thenReturn(Optional.empty());

        //WHEN
        assertThrows(RoleNotFoundException.class, ()-> service.addUser(request));

        //THEN
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_whenUserExists_returnUser(){
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(mapper.fromUserResponse(user)).thenReturn(new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail()));

        UserResponse response = service.getUserById(1);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("Wick", response.getLastName());
        assertEquals("email@email.com", response.getEmail());
    }

    @Test
    void getUserById_whenUserNotFound_throwException(){
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, ()-> service.getUserById(1));

        verify(mapper, never()).fromUserResponse(any());
    }

    @Test
    void getAllUsers_whenSuccessful_returnUsers(){
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(mapper.fromUserResponse(user)).thenReturn(userResponse);

        List<UserResponse> responses = service.getAllUsers();

        assertEquals(1, responses.size());

        verify(mapper, times(1)).fromUserResponse(user);
    }

    @Test
    void getUserByEmail_whenSuccessful_returnUser(){
        String email = "email@email.com";

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        UserResponse response = service.getUserByEmail(email);

        assertNotNull(response);
        assertEquals(user.getFirstName(), response.getFirstName());
        assertEquals(user.getLastName(), response.getLastName());
        assertEquals(user.getEmail(), response.getEmail());

        verify(userRepository, times(1)).findUserByEmail(email);
    }

    @Test
    void getUserByEmail_whenNotFound_throwException(){
        String email = "email@email.com";

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, ()-> service.getUserByEmail(email));

        verify(userRepository, times(1)).findUserByEmail(email);
    }

    @Test
    void updateUserUsername_whenSuccessful_returnUpdatedUser(){
        String email = "email@email.com";

        UserUpdateUsername updated = new UserUpdateUsername("Wally", "Smith");

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        service.updateUserUsername(email, updated);

        assertEquals("Wally", user.getFirstName());
        assertEquals("Smith", user.getLastName());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserUsername_whenUserNotFound_throwException(){
        String email = "email@email.com";
        UserUpdateUsername updated = new UserUpdateUsername("Wally", "Smith");

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, ()-> service.updateUserUsername(email, updated));

        verify(userRepository, never()).save(any());
    }
}
