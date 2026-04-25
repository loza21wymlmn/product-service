package com.example.productservice.service;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.exception.ResourceNotFoundException;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found", id));
        return toResponse(product);
    }

    public ProductResponse create(ProductRequest request) {
        Product product = toEntity(request);
        Product savedProduct = productRepository.save(product);
        return toResponse(savedProduct);
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found", id));

        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStockQty(request.getStockQty());
        existingProduct.setCategory(request.getCategory());

        Product updatedProduct = productRepository.save(existingProduct);
        return toResponse(updatedProduct);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product " + id + " not found", id);
        }
        productRepository.deleteById(id);
    }

    private Product toEntity(ProductRequest request) {
        return new Product(
                request.getName(),
                request.getPrice(),
                request.getStockQty(),
                request.getCategory()
        );
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQty(),
                product.getCategory()
        );
    }
}
