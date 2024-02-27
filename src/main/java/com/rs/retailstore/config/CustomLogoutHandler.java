package com.rs.retailstore.config;

import com.rs.retailstore.model.Token;
import com.rs.retailstore.respository.TokenRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Data
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader= request.getHeader("Authorization");

        if(authHeader==null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        String token = authHeader.substring(7);

        //get stored token from database
        Token storedToken= tokenRepository.findByToken(token).orElse(null);
        //invaliddate the token make logout true
        if(storedToken!=null){
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
            response.setHeader(null, null);
        }
        //save the database


    }
}
