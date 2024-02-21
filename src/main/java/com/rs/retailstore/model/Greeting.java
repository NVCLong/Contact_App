package com.rs.retailstore.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
// annotation Builder use to assign value for greeting
public class Greeting {
    private long id;
    private String content;
}
