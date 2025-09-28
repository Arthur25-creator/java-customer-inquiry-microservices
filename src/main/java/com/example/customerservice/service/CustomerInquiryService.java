package com.example.customerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.customerservice.dto.CustomerInquiryResponse;
import com.example.customerservice.exception.CustomerNotFoundException;

@Service
public class CustomerInquiryService {

	@Autowired
    private RestTemplate restTemplate;

    private final String ACCOUNT_SERVICE_URL = "http://localhost:8080/api/v1/account"; 

    public CustomerInquiryResponse getCustomerByNumber(Long customerNumber) {
        String url = ACCOUNT_SERVICE_URL + "/" + customerNumber;

        try {
            ResponseEntity<CustomerInquiryResponse> response =
                restTemplate.getForEntity(url, CustomerInquiryResponse.class);

            return response.getBody();

        } catch (HttpClientErrorException.NotFound e) {
            throw new CustomerNotFoundException("Customer not found");
        } catch (Exception e) {
            throw new RuntimeException("Internal Server Error: " + e.getMessage(), e);
        }
    }

}

