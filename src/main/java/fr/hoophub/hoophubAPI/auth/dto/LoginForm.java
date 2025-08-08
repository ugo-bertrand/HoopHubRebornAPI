package fr.hoophub.hoophubAPI.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginForm {
    @NotBlank(message = "The email is required to authenticate")
    private String email;

    @NotBlank(message = "The password is required to authenticate")
    private String password;
}
