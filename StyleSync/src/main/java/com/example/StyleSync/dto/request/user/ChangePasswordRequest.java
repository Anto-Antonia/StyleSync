package com.example.StyleSync.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Old password must not be empty.")
    private String oldPassword;

    @NotBlank(message = "New password must not be empty.")
    private String newPassword;
}
