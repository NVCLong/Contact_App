package com.rs.retailstore.service;

import com.rs.retailstore.model.AuthenticationResponse;
import com.rs.retailstore.model.Customer;
import com.rs.retailstore.respository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerService  {
    @Autowired
     CustomerRepository customerRepository;

    @Autowired
    JwtService jwtService;


    public AuthenticationResponse createCustomer(Customer request) {
        System.out.println(request);
        System.out.println(customerRepository.findByUsername("admin"));
        if(!customerRepository.findByUsername(request.getUsername()).isEmpty()) {
            return new AuthenticationResponse(null, "User is already created");
        }
        Customer customer= new Customer();
        customer.setUsername(request.getUsername());
        customer.setPassword(request.getPassword());
        customer.setRole(request.getRole());
        customerRepository.save(customer);
        return new AuthenticationResponse("Create user successfully with the Username "+ customer.getUsername());

    }

}
