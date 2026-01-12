package com.example.StyleSync.service;

import com.example.StyleSync.dto.response.product.ProductResponse;
import com.example.StyleSync.dto.response.role.RoleResponse;
import com.example.StyleSync.dto.response.user.UserResponse;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.entity.Role;
import com.example.StyleSync.entity.User;
import com.example.StyleSync.mapper.ProductMapper;
import com.example.StyleSync.mapper.UserRoleMapper;
import com.example.StyleSync.repository.ProductRepository;
import com.example.StyleSync.repository.RoleRepository;
import com.example.StyleSync.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private ProductMapper productMapper;
    @InjectMocks
    private UserRoleServiceImpl service;

    private User user;
    private Role role;
    private Product product;

    private UserResponse userResponse;
    private RoleResponse roleResponse;
    private ProductResponse productResponse;

    @BeforeEach
    void setup(){
        role = new Role();
        role.setId(1);
        role.setName("user");

        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Wick");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setRole(role);
        user.set
    }


}
