package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void findById_ReturnsProduct_WhenExists() {
        // Arrange
        Long productId = 1L;
        Product mockProduct = new Product("Test Product", 100.0);
        mockProduct.setId(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        // Act
        Optional<Product> result = productService.findById(productId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
        assertEquals(100.0, result.get().getPrice());
    }

    @Test
    void findById_ReturnsEmpty_WhenDoesNotExist() {
        // Arrange
        Long productId = 99L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = productService.findById(productId);

        // Assert
        assertTrue(result.isEmpty());
    }
}
