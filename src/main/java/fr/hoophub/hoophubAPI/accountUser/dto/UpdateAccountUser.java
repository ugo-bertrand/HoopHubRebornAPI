package fr.hoophub.hoophubAPI.accountUser.dto;

import lombok.Data;

@Data
public class UpdateAccountUser {
    private String username;

    private String email;

    private String phone;

    private String password;

    private String firstname;

    private String lastname;
}
