package com.rs.retailstore.Controller;


import com.rs.retailstore.model.Greeting;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class CustomerGreeting {
    private final AtomicLong atomicLong= new AtomicLong();
    private static final  String greetingContent= "Hello, %s %s";
    @GetMapping("/")
    public Greeting greeting(@RequestParam(value="gender", defaultValue = "0") boolean gender, @RequestParam(value="customerName", defaultValue = "customer") String customerName){
        return Greeting.builder()
                .id(atomicLong.incrementAndGet())
                .content(String.format(greetingContent, gender ? "Mr":"Ms",customerName))
                .build();
    }
}
