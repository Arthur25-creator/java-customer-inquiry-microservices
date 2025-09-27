package com.example.customerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.customerservice.dto.CustomerInquiryResponse;
import com.example.customerservice.dto.ErrorResponse;

@Service
public class CustomerInquiryService {

    @Autowired
    private RestTemplate restTemplate;

    private final String ACCOUNT_SERVICE_URL = "http://localhost:8080/api/v1/account"; 

    public ResponseEntity<?> getCustomerByNumber(Long customerNumber) {
        String url = ACCOUNT_SERVICE_URL + "/" + customerNumber;

        try {
            ResponseEntity<CustomerInquiryResponse> response =
                restTemplate.getForEntity(url, CustomerInquiryResponse.class);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(401).body(
                new ErrorResponse(401, "Customer not found")
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new ErrorResponse(500, "Internal Server Error: " + e.getMessage())
            );
        }
    }
}

