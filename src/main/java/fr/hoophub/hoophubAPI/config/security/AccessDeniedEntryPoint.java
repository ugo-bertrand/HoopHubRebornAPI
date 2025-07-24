package fr.hoophub.hoophubAPI.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.hoophub.hoophubAPI.common.ExceptionResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Configuration
@Slf4j
public class AccessDeniedEntryPoint implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message("You are not allowed to access this resource, please try again with a valid account")
                .status(HttpStatus.FORBIDDEN.toString())
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), exceptionResponse);
    }
}
