package com.example.StyleSync.configuration;

import com.example.StyleSync.entity.Role;
import com.example.StyleSync.entity.User;
import com.example.StyleSync.repository.RoleRepository;
import com.example.StyleSync.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initRolesAndAdmin(RoleRepository repository, UserRepository userRepository, PasswordEncoder encoder){
        return args -> {
            Role userRole = repository.findRoleByName("ROLE_USER")
                    .orElseGet(()-> repository.save(new Role("ROLE_USER")));
            Role adminRole = repository.findRoleByName("ROLE_ADMIN")
                    .orElseGet(()-> repository.save(new Role("ROLE_ADMIN")));

            userRepository.findUserByEmail("admin@stylesync.dev").orElseGet(()->{
                User admin = new User();
                admin.setEmail("admin@stylesync.dev");
                admin.setPassword(encoder.encode("Admin@123")); // default password
                admin.setFirstName("Admin");
                admin.setLastName("Admin");
                admin.setRole(adminRole);
                return userRepository.save(admin);
            });
        };
    }
}
