package com.rs.retailstore.model;

import lombok.Data;

@Data

public class AuthenticationResponse {
    private  String message;
    private String token;


    public AuthenticationResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }
    public AuthenticationResponse( String message){
        this.message = message;
        this.token = null;
    }
}
