package com.rs.retailstore.respository;

import com.rs.retailstore.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer> {
    Optional<Contact> findById(int id);
    Optional<Contact> findByPhone(String phone);

}
