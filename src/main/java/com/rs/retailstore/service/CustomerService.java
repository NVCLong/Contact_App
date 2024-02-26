package com.rs.retailstore.service;

import com.rs.retailstore.model.AuthenticationResponse;
import com.rs.retailstore.model.Customer;
import com.rs.retailstore.respository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
public class CustomerService  {
    @Autowired
     CustomerRepository customerRepository;

    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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
        customer.setId(random.nextLong(0,100000));
        System.out.println(customer);
        customerRepository.save(customer);
        return new AuthenticationResponse("Create user successfully with the Username "+ customer.getUsername());

    }

    public AuthenticationResponse loginUser(Customer request){
        System.out.println("login");
        System.out.println(request.getPassword());
        System.out.println(request);
        System.out.println(passwordEncoder.encode(request.getPassword()));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        if(customerRepository.findByUsername(request.getUsername()).isEmpty()){
            return new AuthenticationResponse(null,"Username is uncorrected");
        }
        Customer customer = customerRepository.findByUsername(request.getUsername()).get(0);
            String token= jwtService.generateToken(customer);
            return new AuthenticationResponse(token,"This is a  token and  user name is "+ customer.getUsername());
    }

}
