package com.example.customerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.customerservice.dto.CustomerInquiryResponse;
import com.example.customerservice.exception.CustomerNotFoundException;

@Service
public class CustomerInquiryService {
	@Value("${account-service.base-url}")
	private String accountServiceBaseUrl;

	@Autowired
    private RestTemplate restTemplate;

    public CustomerInquiryResponse getCustomerByNumber(Long customerNumber) {
    	String url = accountServiceBaseUrl + "/api/v1/account/" + customerNumber;

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

