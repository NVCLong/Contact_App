package com.rs.retailstore.service;

import com.rs.retailstore.model.Customer;
import com.rs.retailstore.respository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService  {
    @Autowired
     CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        return  customerRepository.save(customer);
    }
}
