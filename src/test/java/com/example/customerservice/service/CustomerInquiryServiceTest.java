package com.example.customerservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.example.customerservice.dto.CustomerInquiryResponse;
import com.example.customerservice.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureWebClient
class CustomerInquiryServiceTest {

    @Autowired
    private CustomerInquiryService inquiryService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void getCustomerByNumber_success() throws Exception {
        Long customerNumber = 123L;
        String url = "http://localhost:8080/api/v1/account/" + customerNumber;

        CustomerInquiryResponse mockResponse = new CustomerInquiryResponse();
        mockResponse.setCustomerNumber(customerNumber);
        mockResponse.setTransactionStatusCode(302);
        mockResponse.setTransactionStatusDescription("Customer Account found");

        ObjectMapper objectMapper = new ObjectMapper();

        mockServer.expect(ExpectedCount.once(), requestTo(url))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(
                objectMapper.writeValueAsString(mockResponse),
                MediaType.APPLICATION_JSON
            ));

        ResponseEntity<?> response = inquiryService.getCustomerByNumber(customerNumber);

        assertEquals(302, ((CustomerInquiryResponse) response.getBody()).getTransactionStatusCode());
        mockServer.verify();
    }

    @Test
    void getCustomerByNumber_notFound() {
        Long customerNumber = 999L;
        String url = "http://localhost:8080/api/v1/account/" + customerNumber;

        mockServer.expect(requestTo(url))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));

        ResponseEntity<?> response = inquiryService.getCustomerByNumber(customerNumber);

        assertEquals(401, ((ErrorResponse) response.getBody()).getTransactionStatusCode());
        assertEquals("Customer not found", ((ErrorResponse) response.getBody()).getTransactionStatusDescription());
        mockServer.verify();
    }

    @Test
    void getCustomerByNumber_internalServerError() {
        Long customerNumber = 123L;
        String url = "http://localhost:8080/api/v1/account/" + customerNumber;

        mockServer.expect(requestTo(url))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withServerError());

        ResponseEntity<?> response = inquiryService.getCustomerByNumber(customerNumber);

        assertEquals(500, ((ErrorResponse) response.getBody()).getTransactionStatusCode());
        assertTrue(((ErrorResponse) response.getBody()).getTransactionStatusDescription().contains("Internal Server Error"));
        mockServer.verify();
    }
}
