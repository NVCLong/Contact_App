package com.rs.retailstore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

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
