package com.rs.retailstore.respository;

import com.rs.retailstore.model.ContactList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ContactRelationshipRepo extends CrudRepository<ContactList, Integer> {
    @Query(value= """
select cl.contact from ContactList  cl join Contact c on cl.userId=c.id
""")
    List<ContactList> findContactListById(Integer userId);
    List<ContactList> findContactListByUserId(Integer userId);
}
