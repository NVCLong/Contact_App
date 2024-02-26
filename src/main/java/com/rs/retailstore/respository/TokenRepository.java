package com.rs.retailstore.respository;

import com.rs.retailstore.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer>
{
    @Query(value="""
select t from Token t  join Customer c on t.customer.id = c.id
where t.customer.id = :userId and t.isLoggedOut = false
""")
    List<Token> findAllTokensByUser(Integer userId);

    Optional<Token> findByToken(String token);
}
