package fr.hoophub.hoophubAPI.auth;

import fr.hoophub.hoophubAPI.accountUser.AccountUser;
import fr.hoophub.hoophubAPI.accountUser.AccountUserRepository;
import fr.hoophub.hoophubAPI.auth.dto.AuthenticationDto;
import fr.hoophub.hoophubAPI.auth.dto.LoginForm;
import fr.hoophub.hoophubAPI.common.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final AccountUserRepository accountUserRepository;
    private final JwtService jwtService;

    @Override
    public AuthenticationDto login(LoginForm loginForm) throws Exception {
        Authentication authentication;
        final AccountUser accountUser;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
        }
        catch (AuthenticationException e){
            throw new BaseException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            throw new BaseException("An Internal Server Error occured while trying to authenticate", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<AccountUser> optionalAccountUser = accountUserRepository.findByEmail(((AccountUser) (authentication.getPrincipal())).getEmail());
        if(optionalAccountUser.isEmpty()){
            throw new BaseException("The user with the email " + loginForm.getEmail() + " does not exist", HttpStatus.NOT_FOUND);
        }
        accountUser = optionalAccountUser.get();
        String accessToken = jwtService.generateToken(accountUser);
        return AuthenticationDto.builder().token(accessToken).email(accountUser.getEmail()).build();
    }
}