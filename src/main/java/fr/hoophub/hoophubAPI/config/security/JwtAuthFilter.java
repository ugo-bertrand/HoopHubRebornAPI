package fr.hoophub.hoophubAPI.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.hoophub.hoophubAPI.accountUser.AccountUser;
import fr.hoophub.hoophubAPI.accountUser.AccountUserService;
import fr.hoophub.hoophubAPI.auth.JwtService;
import fr.hoophub.hoophubAPI.common.BaseException;
import fr.hoophub.hoophubAPI.common.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    private final JwtService jwtService;

    private final AccountUserService accountUserService;

    @Value("${api.key}")
    private String apiKey;

    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String apiKeyHeader = request.getHeader("API-Key");
        if(authHeader == null && !authHeader.startsWith(BEARER)){
            if(apiKeyHeader == null || !apiKeyHeader.equals(apiKey)){
                filterChain.doFilter(request, response);
                return;
            }
        }
        try {
            final String jwtToken = authHeader.substring(BEARER.length());
            final String email = jwtService.extractEmail(jwtToken);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(email != null && authentication == null){
                AccountUser accountUser = accountUserService.getAccountUserByEmail(email);
                if(jwtService.isTokenValid(jwtToken,accountUser)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(accountUser,null,accountUser.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                throw new SignatureException("Invalid token");
            }
            filterChain.doFilter(request, response);
        }
        catch (SignatureException e){
            log.error("Invalid JWT token: {}",e.getMessage());
            ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Invalid JWT token, please try again with a valid token")
                    .status(HttpStatus.UNAUTHORIZED.toString()).build();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getOutputStream(), exceptionResponse);
        }
        catch (ExpiredJwtException e){
            log.error("The JWT token is expired, please try again with a more recent token: {}",e.getMessage());
            ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Expired JWT token, please try again with a more recent token")
                    .status(HttpStatus.UNAUTHORIZED.toString()).build();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getOutputStream(), exceptionResponse);
        }
        catch (BaseException e) {
            log.error("The email in the token does not exist, please try again with a new token: {}",e.getMessage());
            ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("The email in the token does not exist, please try again with a new token")
                    .status(HttpStatus.UNAUTHORIZED.toString()).build();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getOutputStream(), exceptionResponse);
        }
    }
}
