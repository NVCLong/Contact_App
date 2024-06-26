package com.rs.retailstore.service;

import com.rs.retailstore.model.Customer;
import com.rs.retailstore.respository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

;

@Service
public class JwtService {
    private final String SECRET_KEY= "f241085f6e34a99c4da1501d55025d8a7d06b1eaababb3531220aff24c211693";
    private  final TokenRepository tokenRepository;

    public JwtService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public <T> T extractClaim(String token, Function<Claims,T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }


    public boolean isValid(String token, UserDetails user){
        String username= extractUsername(token);
        System.out.println(isTokenExpired(token));
        System.out.println(username.equals(user.getUsername()));
        boolean isValidToken= tokenRepository.findByToken(token)
                .map(t-> !t.isLoggedOut()).orElse(false);
        System.out.println(isValidToken);
        System.out.println((username.equals(user.getUsername())&&!isTokenExpired(token)&&!isValidToken));

        return (username.equals(user.getUsername())&&!isTokenExpired(token)&&isValidToken);
    }

    public boolean isTokenExpired(String token){
        System.out.println(extractExpiration(token));
        System.out.println(new Date());
        return extractExpiration(token).before(new Date());
    }

    private java.util.Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public String extractId(String token){ return extractClaim(token,Claims::getId);}



    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(Customer customer){
        String token= Jwts.
                builder()
                .subject(customer.getUsername())
                .id(customer.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
                .signWith(getSigninKey())
                .compact();
        return token;
    }
    public SecretKey getSigninKey(){
        byte[] signature= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(signature);
    }


}
