package com.rs.retailstore.Controller;


import com.rs.retailstore.model.Greeting;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class CustomerGreeting {
    private final AtomicLong atomicLong= new AtomicLong();
    private static final  String greetingContent= "Hello, %s %s";
    @GetMapping("/")
    public ResponseEntity<String> greeting(@RequestParam(value="gender", defaultValue = "0") boolean gender, @RequestParam(value="customerName", defaultValue = "customer") String customerName){
        return ResponseEntity.ok(Greeting.builder()
                .id(atomicLong.incrementAndGet())
                .content(String.format(greetingContent, gender ? "Mr":"Ms",customerName))
                .build().toString());
    }
}
