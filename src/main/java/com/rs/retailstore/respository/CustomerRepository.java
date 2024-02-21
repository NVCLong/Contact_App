package com.rs.retailstore.respository;

import com.rs.retailstore.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Integer> {
    List<Customer> findByUsername(String username);

}
