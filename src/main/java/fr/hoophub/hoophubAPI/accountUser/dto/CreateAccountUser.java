package fr.hoophub.hoophubAPI.accountUser.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CreateAccountUser {
    @NotBlank(message = "The username is required to create an account")
    @Size(min = 3, max = 25, message = "The username must be between 3 and 25 characters")
    private String username;

    @NotBlank(message = "The email is required to create an account")
    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,3}", message = "The email must be valid with '@', '.' and a valid domain")
    private String email;

    @NotBlank(message = "The password is required to create an account")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-_/]).{8,}$",
    message = "The password must have at least 8 characters, one uppercase letter, one lowercase letter, one number and one special character in the following list : #?!@$%^&*-_/")
    private String password;

    @NotBlank(message = "The firstname is required to create an account")
    @Size(min = 2, max = 50, message = "The firstname must be between 2 and 50 characters")
    private String firstname;

    @NotBlank(message = "The lastname is required to create an account")
    @Size(min = 2, max = 50, message = "The lastname must be between 2 and 50 characters")
    private String lastname;
}
