package com.rs.retailstore.Controller;

import com.rs.retailstore.model.Customer;
import com.rs.retailstore.service.CustomerService;
import jakarta.servlet.annotation.HttpConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {

    @Autowired
    CustomerService customerService;
    @PostMapping("/register")
    public ResponseEntity<String>  register(@RequestBody Customer customer) {
        System.out.println(customer);
        try{
            Customer customer1= customerService.createCustomer(customer);
            if(customer1.getId()>0){
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Create successful the user"+ customer1.getUsername());
            }else{
                System.out.println("Have error while create user");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server crash by the exception internal server error");
        }
        return  null;
    }

}
