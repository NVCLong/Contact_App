package com.rs.retailstore.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    private Long id;

    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType type;

    private boolean isLoggedOut;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private  Customer customer;
}
