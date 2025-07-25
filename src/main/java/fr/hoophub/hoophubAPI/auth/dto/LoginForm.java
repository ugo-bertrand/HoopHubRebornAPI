package fr.hoophub.hoophubAPI.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {
    @NotBlank(message = "The email is required to authenticate")
    private String email;

    @NotBlank(message = "The password is required to authenticate")
    private String password;
}
