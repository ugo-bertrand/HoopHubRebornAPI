package fr.hoophub.hoophubAPI.auth;

import fr.hoophub.hoophubAPI.auth.dto.AuthenticationDto;
import fr.hoophub.hoophubAPI.auth.dto.LoginForm;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    AuthenticationDto login(LoginForm loginForm) throws Exception;
}