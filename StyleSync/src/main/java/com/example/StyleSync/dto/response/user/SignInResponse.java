package com.example.StyleSync.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
    private String firstName;
    private String email;
    private List<String> roleName;
    private String token;
}
