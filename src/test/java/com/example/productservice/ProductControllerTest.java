package com.example.productservice;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product savedProduct;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        Product product = new Product("Test Laptop", 1000.00, 5, "Electronics");
        savedProduct = productRepository.save(product);
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Laptop")));
    }

    @Test
    void shouldGetProductById() throws Exception {
        mockMvc.perform(get("/api/v1/products/{id}", savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Laptop")));
    }

    @Test
    void shouldReturn404WhenGetWithInvalidId() throws Exception {
        mockMvc.perform(get("/api/v1/products/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("Resource Not Found")))
                .andExpect(jsonPath("$.detail", containsString("not found")));
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest("New Phone", 800.00, 10, "Electronics");

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name", is("New Phone")));
    }

    @Test
    void shouldReturn400WhenCreateWithBlankName() throws Exception {
        ProductRequest request = new ProductRequest("", 800.00, 10, "Electronics");

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.title", is("Validation Error")));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        ProductRequest request = new ProductRequest("Updated Laptop", 1500.00, 20, "Electronics");

        mockMvc.perform(put("/api/v1/products/{id}", savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Laptop")))
                .andExpect(jsonPath("$.price", is(1500.00)));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/v1/products/{id}", savedProduct.getId()))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/v1/products/{id}", savedProduct.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenDeleteWithInvalidId() throws Exception {
        mockMvc.perform(delete("/api/v1/products/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
