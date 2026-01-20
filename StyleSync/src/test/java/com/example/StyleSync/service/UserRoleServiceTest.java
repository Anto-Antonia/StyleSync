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
import com.example.StyleSync.exceptions.role.RoleAlreadyExistsException;
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
        product.setPrice(10.99);
        product.setQuantity(1);

        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Wick");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setRole(role);
        user.setFavoriteProducts(new ArrayList<>());

        roleResponse = new RoleResponse("ROLE_USER");
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

    @Test
    void addItemToFavorite_whenNotAlreadyFavorite_addItemToFavorite(){
        Integer itemId = 1;
        String email = "email@email.com";

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(productRepository.findById(itemId)).thenReturn(Optional.of(product));

        service.addItemToFavorite(email, itemId);
        assertTrue(user.getFavoriteProducts().contains(product));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void addItemToFavorite_whenAlreadyFavorite_addItemToFavorite(){
        Integer itemId = 1;
        String email = "email@email.com";

        user.getFavoriteProducts().add(product);

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(productRepository.findById(itemId)).thenReturn(Optional.of(product));

        service.addItemToFavorite(email, itemId);

        verify(userRepository, never()).save(any());
    }

    @Test
    void addItemToFavorite_whenItemNotFound_throwException(){
        Integer itemId = 1;
        String email = "email@email.com";

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(productRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, ()-> service.addItemToFavorite(email, itemId));

        verify(userRepository, never()).save(any());
    }

    @Test
    void addItemToFavorite_whenUserDoesNotExist_throwException(){
        Integer itemId = 1;
        String email = "email@email.com";

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, ()-> service.addItemToFavorite(email, itemId));

        verify(productRepository, never()).findById(itemId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void removeItemFromFav_whenSuccessful_removeItemFromFav(){
        Integer itemId = 1;
        String email = "email@email.com";

        user.getFavoriteProducts().add(product);

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(productRepository.findById(itemId)).thenReturn(Optional.of(product));

        service.removeItemFromFavorite(email, itemId);

        assertFalse(user.getFavoriteProducts().contains(product));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void removeItemFromFav_whenItemIsNotInFav_doNothing(){
        Integer itemId = 1;
        String email = "email@email.com";

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(productRepository.findById(itemId)).thenReturn(Optional.of(product));

        service.removeItemFromFavorite(email, itemId);

        verify(userRepository, never()).save(any());
    }

    @Test
    void getFavoriteProducts_whenSuccessful_returnProduct(){
        String email = "email@email.com";

        user.getFavoriteProducts().add(product); // making sure the user has a fav product

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        List<FavoriteProductResponse> favorites = service.getFavoriteProducts(email);

        assertEquals(1, favorites.size());
        assertEquals(product.getProductName(), favorites.get(0).getProductName());
        assertEquals(product.getPrice(), favorites.get(0).getPrice());
        assertEquals(product.getQuantity(), favorites.get(0).getQuantity());

        verify(userRepository, times(1)).findUserByEmail(email);
    }

    @Test
    void deleteUser_whenSuccessful_deleteUser(){
        Integer userId = 1;

        service.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void addRole_whenRoleDoesNotExist_returnRole(){
        RoleRequest request = new RoleRequest("ROLE_ADMIN");

        Role role = new Role("ROLE_ADMIN");

        when(roleRepository.findRoleByName("ROLE_ADMIN")).thenReturn(Optional.empty());
        when(mapper.fromRoleRequest(request)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);

        Role result = service.addRole(request);

        assertNotNull(result);
        assertEquals("ROLE_ADMIN", result.getName());

        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void addRole_whenRoleExists_throwException(){
        RoleRequest request = new RoleRequest("ROLE_USER");

        Role role = new Role("ROLE_USER");

        when(roleRepository.findRoleByName("ROLE_USER")).thenReturn(Optional.of(role));
        assertThrows(RoleAlreadyExistsException.class, ()-> service.addRole(request));

        verify(roleRepository, never()).save(role);
        verify(mapper, never()).fromRoleRequest(any());
    }

    @Test
    void addRoleToUser_whenSuccessful_addRoleToUser(){
        String roleName = "ROLE_USER";
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findRoleByName(roleName)).thenReturn(Optional.of(role));

        service.addRoleToUser(userId, roleName);

        assertEquals(role, user.getRole());

        verify(userRepository, times(1)).findById(userId);
        verify(roleRepository, times(1)).findRoleByName(roleName);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void addRoleToUser_whenUserNotFound_throwException(){
        String roleName = "ROLE_USER";
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, ()-> service.addRoleToUser(userId, roleName));

        verify(roleRepository, never()).findRoleByName(roleName);
        verify(userRepository, never()).save(any());
    }

    @Test
    void addRoleToUser_whenRoleNotFound_throwException(){
        String roleName = "ROLE_USER";
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findRoleByName(roleName)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, ()-> service.addRoleToUser(userId, roleName));

        verify(userRepository, never()).save(any());
    }

    @Test
    void getRole_whenSuccessful_returnRole(){
        Integer roleId = 1;

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(mapper.fromRoleResponse(role)).thenReturn(roleResponse);

        RoleResponse response = service.getRole(roleId);

        assertNotNull(response);
        assertEquals(roleResponse.getRoleName(), response.getRoleName());

        verify(roleRepository, times(1)).findById(roleId);
        verify(mapper, times(1)).fromRoleResponse(role);
    }

    @Test
    void getRole_whenRoleDoesNotExist_throwException(){
        Integer roleId = 1;

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, ()-> service.getRole(roleId));

        verify(mapper, never()).fromRoleResponse(any());
    }

    @Test
    void getAllRoles_whenSuccessful_returnRoles(){
        when(roleRepository.findAll()).thenReturn(List.of(role));
        when(mapper.fromRoleResponse(role)).thenReturn(roleResponse);

        List<RoleResponse> roles = service.getAllRoles();

        assertEquals("ROLE_USER", roles.get(0).getRoleName());

        verify(roleRepository, times(1)).findAll();
        verify(mapper, times(1)).fromRoleResponse(role);
    }
}
