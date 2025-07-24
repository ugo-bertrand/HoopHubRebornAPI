package fr.hoophub.hoophubAPI.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.hoophub.hoophubAPI.common.ExceptionResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Configuration
public class UnauthenticatedEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if(!response.isCommitted()){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("You are not authenticated to access this resource.")
                    .status(HttpStatus.UNAUTHORIZED.toString())
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), exceptionResponse);
        }
    }
}
