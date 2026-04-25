package com.example.productservice.dto;

public class ProductResponse {

    private Long id;
    private String name;
    private double price;
    private int stockQty;
    private String category;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, double price, int stockQty, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQty = stockQty;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
