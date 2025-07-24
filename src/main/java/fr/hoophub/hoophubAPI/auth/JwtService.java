package fr.hoophub.hoophubAPI.auth;

import fr.hoophub.hoophubAPI.accountUser.AccountUser;
import fr.hoophub.hoophubAPI.accountUser.AccountUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${auth.jwt.secret}")
    private String accessTokenSecret;

    @Value("${auth.jwt.expiration}")
    private Long accessTokenExpiration;

    private final AccountUserRepository accountUserRepository;

    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(AccountUser accountUser){
        return generateToken(new HashMap<>(), accountUser);
    }

    public String generateToken(Map<String, Object> extraClaims, AccountUser accountUser){
        return buildToken(extraClaims, accountUser, accessTokenExpiration);
    }

    public String buildToken(Map<String, Object> extraClaims, AccountUser accountUser, Long accessTokenExpiration){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(accountUser.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, AccountUser accountUser){
        final String email = extractEmail(token);
        return (email.equals(accountUser.getEmail()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Key getSignInKey() {
        byte[] keyBites = Decoders.BASE64.decode(accessTokenSecret);
        return Keys.hmacShaKeyFor(keyBites);
    }

}
