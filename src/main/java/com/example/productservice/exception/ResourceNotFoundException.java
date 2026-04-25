package com.example.productservice.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    private final Long resourceId;
    
    public ResourceNotFoundException(String message, Long resourceId) {
        super(message);
        this.resourceId = resourceId;
    }
    
    public Long getResourceId() {
        return resourceId;
    }
}
