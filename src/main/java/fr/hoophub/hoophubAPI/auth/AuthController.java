package fr.hoophub.hoophubAPI.auth;

import fr.hoophub.hoophubAPI.accountUser.AccountUser;
import fr.hoophub.hoophubAPI.accountUser.AccountUserService;
import fr.hoophub.hoophubAPI.accountUser.dto.CreateAccountUser;
import fr.hoophub.hoophubAPI.auth.dto.AuthenticationDto;
import fr.hoophub.hoophubAPI.auth.dto.LoginForm;
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

    private final AuthService authService;

    @PostMapping("/createAccount")
    public ResponseEntity<AccountUser> createUserAccount(@RequestBody @Valid CreateAccountUser accountUserBody){
        return new ResponseEntity<>(accountUserService.createAccountUser(accountUserBody),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDto> login(@RequestBody @Valid LoginForm loginForm) throws Exception {
        return new ResponseEntity<>(authService.login(loginForm),HttpStatus.OK);
    }
}
