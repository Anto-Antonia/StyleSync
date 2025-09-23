package com.example.StyleSync.service.security;

import com.example.StyleSync.entity.User;
import com.example.StyleSync.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = repository.findUserByEmail(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return new UserDetailsImpl(user);
        } else{
            throw new UsernameNotFoundException("Invalid email");
        }
    }
}
