package com.rs.retailstore.service;

import com.rs.retailstore.model.AuthenticationResponse;
import com.rs.retailstore.model.Customer;
import com.rs.retailstore.model.Token;
import com.rs.retailstore.respository.CustomerRepository;
import com.rs.retailstore.respository.TokenRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
@Data
public class CustomerService {
    @Autowired
     CustomerRepository customerRepository;

    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Autowired
    TokenRepository tokenRepository;

    public CustomerService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    public AuthenticationResponse createCustomer(Customer request) {
        System.out.println("register");
        Random random = new Random();
        System.out.println(request);
        System.out.println(customerRepository.findByUsername("admin"));
        if(!customerRepository.findByUsername(request.getUsername()).isEmpty()) {
            return new AuthenticationResponse(null, "User is already created");
        }
        Customer customer= new Customer();

        customer.setUsername(request.getUsername());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setRole(request.getRole());
        customer.setId(random.nextInt(0,100000));
        System.out.println(customer);
        customerRepository.save(customer);
        String token= jwtService.generateToken(customer);
        return new AuthenticationResponse("Create user successfully with the Username "+ customer.getUsername());

    }

    private void saveToken(String token, Customer customer) {
        Token newToken= new Token();
        Random random = new Random();
        newToken.setToken(token);
        newToken.setLoggedOut(false);
        newToken.setId(random.nextLong(0,100000));
        newToken.setCustomer(customer);
        tokenRepository.save(newToken);
    }

    public AuthenticationResponse loginUser(Customer request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        Customer customer = customerRepository.findByUsername(request.getUsername()).get(0);
            String token= jwtService.generateToken(customer);
            saveToken(token,customer);
            return new AuthenticationResponse(token,"This is a  token and  user name is "+ customer.getUsername());
    }


    public void revokeAllToken(Customer customer){
        List<Token> validTokensListByUser= tokenRepository.findAllTokensByUser(customer.getId());

        if(!validTokensListByUser.isEmpty()){
            validTokensListByUser.forEach(t->{
                t.setLoggedOut(true);
            });
        }
        tokenRepository.saveAll(validTokensListByUser);
    }

}
