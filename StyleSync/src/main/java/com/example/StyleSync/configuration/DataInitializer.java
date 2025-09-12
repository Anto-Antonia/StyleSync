package com.example.StyleSync.configuration;

import com.example.StyleSync.entity.Role;
import com.example.StyleSync.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initRoles(RoleRepository repository){
        return args -> {
            if(repository.findRoleByName("ROLE_USER").isEmpty()){
                repository.save(new Role("ROLE_USER"));
            }
            if(repository.findRoleByName("ROLE_ADMIN").isEmpty()){
                repository.save(new Role("ROLE_ADMIN"));
            }
        };
    }
}
