package com.rs.retailstore.service;

import com.rs.retailstore.model.Customer;
import com.rs.retailstore.respository.CustomerRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Log
public class RetailsStoreUserDetail implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Customer> customerList= customerRepository.findByUsername(username);
        System.out.println(customerList);
        if(customerList.isEmpty()){
            throw new UsernameNotFoundException("Can not find the user with username"+ username);
        }
        username= customerList.get(0).getUsername();
        String password= customerList.get(0).getPassword();
        ArrayList<GrantedAuthority> authorityArrayList= new ArrayList<GrantedAuthority>();
        authorityArrayList.add(new SimpleGrantedAuthority(customerList.get(0).getRole()));
        return new User(username,password,authorityArrayList);
    }
}
