package com.rs.retailstore.Controller;

import com.rs.retailstore.model.AuthenticationResponse;
import com.rs.retailstore.model.Customer;
import com.rs.retailstore.service.CustomerService;
import jakarta.servlet.annotation.HttpConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {


    CustomerService customerService;

    public RegisterController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(value="/register", produces="application/json", consumes = "application/json")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody Customer request) {
        if(request.getRole() == null){
            request.setRole("user");
        }
        return ResponseEntity.ok(customerService.createCustomer(request));
    }
    @PostMapping(value="/login", produces="application/json", consumes = "application/json")
    public <response> ResponseEntity<AuthenticationResponse> login(@RequestBody Customer request) {
        return ResponseEntity.ok().body(customerService.loginUser(request));
    }
}
