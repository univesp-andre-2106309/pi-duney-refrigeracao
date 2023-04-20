package com.duneyrefrigeracao.backend.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @GetMapping
    public ResponseEntity<String> GetPage(){
        return ResponseEntity.ok().body("Hello World");

    }

    @PostMapping
    public ResponseEntity<String> PostPage(){

        var object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(object instanceof UserDetails) {
            System.out.println(((UserDetails)object).getUsername());
        }
        return ResponseEntity.ok().body("Hello World");
    }

}
