package com.example.StyleSync.service.security;

import com.example.StyleSync.dto.request.user.RegisterRequest;
import com.example.StyleSync.dto.request.user.SignInRequest;
import com.example.StyleSync.dto.response.user.RegisterResponse;
import com.example.StyleSync.dto.response.user.SignInResponse;
import com.example.StyleSync.entity.Role;
import com.example.StyleSync.entity.User;
import com.example.StyleSync.exceptions.auth.InvalidPasswordException;
import com.example.StyleSync.exceptions.role.RoleNotFoundException;
import com.example.StyleSync.exceptions.user.EmailAlreadyExistsException;
import com.example.StyleSync.jwt.JwtUtils;
import com.example.StyleSync.mapper.UserRoleMapper;
import com.example.StyleSync.repository.RoleRepository;
import com.example.StyleSync.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public void checkEmailAvailable(String email){
        if(!isValidEmail(email)){
            throw new IllegalArgumentException("Invalid email format.");
        }

        if(userRepository.findUserByEmail(email).isPresent()){
            throw new EmailAlreadyExistsException("Email already in use.");
        }
    }

    public void validatePassword(String password){
        if(password == null || password.length() < 6){
            throw new InvalidPasswordException("the password must be at least 6 characters long.");
        }
    }

    public static boolean isValidEmail(String email){
        String EMAIL_REGEX= "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|net|org|edu|gov|co|info|eu|uk|ca|de|fr|it|us|cz|jp|ru|au)$";
        return email.matches(EMAIL_REGEX);
    }

    public void validateFirstName(String firstName){
        if(firstName == null || firstName.length() < 3){
            throw new EmailAlreadyExistsException("The firstname must be at least 3 characters long");
        }
    }

    public RegisterResponse registerUser(RegisterRequest request){
        validateFirstName(request.getFirstName());
        checkEmailAvailable(request.getEmail());
        validatePassword(request.getPassword());

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        Role defaultRole = roleRepository.findRoleByName("ROLE_USER")
                .orElseThrow(()-> new RoleNotFoundException("Default role 'ROLE_USER' not found."));

        user.setRole(defaultRole);

        userRepository.save(user);
        return new RegisterResponse(
                user.getFirstName(),
                user.getEmail(),
                "User registered successfully"
        );
    }

    public SignInResponse signIn(SignInRequest signInRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getEmail(),
                            signInRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findUserByEmail(signInRequest.getEmail())
                    .orElseThrow(()-> new BadCredentialsException("User with email not found."));

            UserDetailsImpl userDetails = UserDetailsImpl.build(user);

            String token = jwtUtils.generateToken(userDetails.getUsername());

            return UserRoleMapper.fromUserDetailsImpl(userDetails, token);
        } catch(AuthenticationException e){
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
