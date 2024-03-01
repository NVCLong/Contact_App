package com.rs.retailstore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)

public class Contact {
    @Id
    private Integer id;
    private String name;
    private String email;
    private String title;
    private  String phone;
    private String address;
    private String status;
    private String photoUrl;
}
