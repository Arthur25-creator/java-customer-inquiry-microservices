package com.example.customerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.customerservice.service.CustomerInquiryService;

@RestController
@RequestMapping("/api/v1/account")
public class CustomerInquiryController {

    @Autowired
    private CustomerInquiryService inquiryService;

    @GetMapping("/{customerNumber}")
    public ResponseEntity<?> getCustomer(@PathVariable Long customerNumber) {
        return inquiryService.getCustomerByNumber(customerNumber);
    }
}

