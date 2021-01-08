package com.example.customer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class CustomerServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	ObjectMapper mapper;
	ArrayList<Customer> customerList;

	String customersJsonPath = "src/test/data/customers.json"; // 4 customers
	String customerJsonPath = "src/test/data/existingCustomer.json"; // 1 customer
	String newCustomerJsonPath = "src/test/data/newCustomer.json"; // 1 customer


	@BeforeEach
	void setUp() throws IOException {
		initializeCustomersData();
	}

	// TEST UTILITIES ----------------------------------------------------

	private void initializeCustomersData() throws IOException {
		mapper = new ObjectMapper();
		File customersFile = new File(customersJsonPath);
		customerList = mapper.readValue(customersFile, new TypeReference<ArrayList<Customer>>() {});
	}

	private String getCustomerJsonString() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File customerFile = new File(customerJsonPath);
		Customer customer = mapper.readValue(customerFile, Customer.class);
		return mapper.writeValueAsString(customer);
	}

	private Customer createCustomerJsonString() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File customerFile = new File(newCustomerJsonPath);
		Customer customer = mapper.readValue(customerFile, Customer.class);
		return customer;
	}

	@Test
	void getCustomers() throws Exception {
		mockMvc.perform(get("/customers")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(mapper.writeValueAsString(customerList)));
	}

	@Test
	void getCustomerById() throws Exception {
		mockMvc.perform(get("/customer/b8a504e8-7cbd-4a54-9a24-dc1832558162")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists());
	}

	@Test
	void createCustomer() throws Exception {
		mockMvc.perform(post("/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.content(mapper.writeValueAsString(createCustomerJsonString())))
				.andExpect(status().isCreated());

	}

}
