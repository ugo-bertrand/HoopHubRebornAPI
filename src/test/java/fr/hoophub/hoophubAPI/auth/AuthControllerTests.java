package fr.hoophub.hoophubAPI.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.hoophub.hoophubAPI.HoophubApiApplication;
import fr.hoophub.hoophubAPI.accountUser.AccountUserService;
import fr.hoophub.hoophubAPI.auth.dto.AuthenticationDto;
import fr.hoophub.hoophubAPI.auth.dto.LoginForm;
import fr.hoophub.hoophubAPI.common.BaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.MOCK, classes = HoophubApiApplication.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AuthControllerTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AccountUserService accountUserService;

    private MockMvc mvc;

    @BeforeEach
    void setUp(){
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void create_account_should_be_success(){
        //todo
    }

    @Test
    void create_account_should_throw_error_because_body_is_not_valid(){
        //todo
    }

    @Test
    void create_account_should_throw_error_because_email_already_exists(){
        //todo
    }

    @Test
    void create_account_should_throw_error_because_username_already_exists(){
        //todo
    }

    @Test
    void create_account_should_thrw_internal_server_error(){
        //todo
    }

    @Test
    void login_should_be_success() throws Exception {
        LoginForm loginForm = LoginForm.builder().email("test@gmail.com").password("Password123@").build();

        AuthenticationDto authenticationDto = AuthenticationDto.builder().token("accessToken").email(loginForm.getEmail()).build();

        when(authService.login(loginForm)).thenReturn(authenticationDto);

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("accessToken"))
                .andExpect(jsonPath("$.email").value(loginForm.getEmail()));
    }

    @Test
    void login_should_throw_error_because_body_is_not_valid() throws Exception {
        LoginForm loginForm = LoginForm.builder().email("").password("Password123@").build();

        AuthenticationDto authenticationDto = AuthenticationDto.builder().token("accessToken").email(loginForm.getEmail()).build();

        when(authService.login(loginForm)).thenReturn(authenticationDto);

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("The email is required to authenticate"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"));
    }

    @Test
    void login_should_throw_error_because_email_does_not_exist() throws Exception {
        LoginForm loginForm = LoginForm.builder().email("test@gmail.com").password("Password123@").build();

        when(authService.login(loginForm)).thenThrow(new BaseException("The user with the email " + loginForm.getEmail() + " does not exist", HttpStatus.NOT_FOUND));

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginForm)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("The user with the email " + loginForm.getEmail() + " does not exist"))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.code").value("404"));
    }

    @Test
    void login_should_throw_error_because_email_password_combination_is_incorrect() throws Exception {
        LoginForm loginForm = LoginForm.builder().email("test@gmail.com").password("Password123@").build();

        when(authService.login(loginForm)).thenThrow(new BaseException("Invalid Password", HttpStatus.UNAUTHORIZED));

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginForm)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid Password"))
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.code").value("401"));
    }

    @Test
    void login_should_throw_internal_server_error() throws Exception {
        LoginForm loginForm = LoginForm.builder().email("test@gmail.com").password("Password123@").build();

        when(authService.login(loginForm)).thenThrow(new BaseException("An Internal Server Error occured while trying to authenticate", HttpStatus.INTERNAL_SERVER_ERROR));

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginForm)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("An Internal Server Error occured while trying to authenticate"))
                .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.code").value("500"));
    }
}
