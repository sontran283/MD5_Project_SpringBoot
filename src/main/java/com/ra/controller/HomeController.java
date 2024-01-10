package com.ra.controller;

import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.service.EmailService;
import com.ra.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private EmailService emailService;

    @RequestMapping("/products")
    public ResponseEntity<?> getAllProduct() {
        return new ResponseEntity<>("list", HttpStatus.OK);
    }

    @GetMapping("/testEmail")
    public ResponseEntity<?> testEmail() {
        emailService.sendMail();
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
