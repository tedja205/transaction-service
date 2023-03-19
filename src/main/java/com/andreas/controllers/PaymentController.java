package com.andreas.controllers;

import com.andreas.models.Constant;
import com.andreas.models.PaymentRequest;
import com.andreas.services.PaymentService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity deposit(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return paymentService.transaction(paymentRequest, request, Constant.DEPOSIT);


    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity withdraw(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
        return paymentService.transaction(paymentRequest, request, Constant.WITHDRAW);
    }

    @GetMapping("/check-balance")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity checkBalance(HttpServletRequest request) {
        return paymentService.checkBalance(request);
    }
}
