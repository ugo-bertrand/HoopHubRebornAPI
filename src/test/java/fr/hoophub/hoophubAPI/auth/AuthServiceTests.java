package fr.hoophub.hoophubAPI.auth;

import fr.hoophub.hoophubAPI.accountUser.AccountUser;
import fr.hoophub.hoophubAPI.accountUser.AccountUserRepository;
import fr.hoophub.hoophubAPI.auth.dto.AuthenticationDto;
import fr.hoophub.hoophubAPI.auth.dto.LoginForm;
import fr.hoophub.hoophubAPI.common.BaseException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AccountUserRepository accountUserRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;


    @Test
    void login_should_be_success() throws Exception {
        LoginForm loginForm = LoginForm.builder().email("test@gmail.com").password("testPassword").build();
        AccountUser accountUser = AccountUser.builder().email("test@gmail.com").build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(accountUser, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(accountUserRepository.findByEmail(any())).thenReturn(Optional.of(accountUser));
        when(jwtService.generateToken(accountUser)).thenReturn("token");

        AuthenticationDto authenticationDto = authService.login(loginForm);

        assertNotNull(authenticationDto);
        assertEquals("token", authenticationDto.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(accountUserRepository, times(1)).findByEmail("test@gmail.com");
        verify(jwtService, times(1)).generateToken(accountUser);
    }

    @Test
    void login_should_throw_authentication_exception() {
        LoginForm loginForm = LoginForm.builder().email("test@gmail.com").password("testPassword").build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationCredentialsNotFoundException("Authentication failed"));

        BaseException exception = assertThrows(BaseException.class, () -> authService.login(loginForm));
        assertEquals("Authentication failed", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
    }

    @Test
    void login_should_throw_not_found_error_because_the_user_does_not_exist() throws Exception {
        LoginForm loginForm = LoginForm.builder().email("test@gmail.com").password("testPassword").build();
        AccountUser accountUser = AccountUser.builder().email("test@gmail.com").build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(accountUser, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(accountUserRepository.findByEmail(any())).thenReturn(Optional.empty());

        BaseException exception = assertThrows(BaseException.class, () -> authService.login(loginForm));

        assertEquals("The user with the email " + loginForm.getEmail() + " does not exist", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_should_throw_internal_server_error(){
        LoginForm loginForm = LoginForm.builder().email("test@gmail.com").password("testPassword").build();
        AccountUser accountUser = AccountUser.builder().email("test@gmail.com").build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(accountUser, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(accountUserRepository.findByEmail(any())).thenReturn(Optional.of(accountUser));
        when(jwtService.generateToken(accountUser)).thenThrow(new JwtException("An error occurred while generating the token"));

        JwtException exception = assertThrows(JwtException.class, () -> authService.login(loginForm));

        assertEquals("An error occurred while generating the token", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(accountUserRepository, times(1)).findByEmail("test@gmail.com");
    }
}
