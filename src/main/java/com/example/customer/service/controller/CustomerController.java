package com.example.customer.service.controller;

import com.example.customer.service.Customer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {

    ObjectMapper mapper;
    ArrayList<Customer> customerList;
    String customersJsonPath = "src/test/data/customers.json"; // 4 customers

    private CustomerController() throws IOException {
        mapper = new ObjectMapper();
        File customersFile = new File(customersJsonPath);
        customerList = mapper.readValue(customersFile, new TypeReference<ArrayList<Customer>>() {});
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers() throws IOException {
        return customerList;
    }

    @GetMapping("/customer/{id}")
    public Customer getCustomerById(@PathVariable String id) throws IOException {
        for (Customer customer : customerList ) {
            if(customer.getId().equals(id)){
                return customer;
            }
        }
     return null;
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) throws IOException {
        return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);
    }
}
