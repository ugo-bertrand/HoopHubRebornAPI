package fr.hoophub.hoophubAPI.auth;

import fr.hoophub.hoophubAPI.accountUser.AccountUserService;
import fr.hoophub.hoophubAPI.accountUser.dto.CreateAccountUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountUserService accountUserService;

    @PostMapping("/createAccount")
    public ResponseEntity<?> createUserAccount(@RequestBody @Valid CreateAccountUser accountUserBody) throws Exception{
        return new ResponseEntity<>(accountUserService.createAccountUser(accountUserBody),HttpStatus.CREATED);
    }
}
