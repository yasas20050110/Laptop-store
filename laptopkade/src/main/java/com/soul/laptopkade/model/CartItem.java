package com.soul.laptopkade.model;

import java.math.BigDecimal;

public class CartItem {
    private Long laptopId;
    private String name;
    private String brand;
    private String price;
    private String imageUrl;
    private int quantity;

    public CartItem() {
    }

    public CartItem(Long laptopId, String name, String brand, String price, String imageUrl, int quantity) {
        this.laptopId = laptopId;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Long getLaptopId() {
        return laptopId;
    }

    public void setLaptopId(Long laptopId) {
        this.laptopId = laptopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getNumericPrice() {
        if (price == null) return 0.0;
        String cleaned = price.replaceAll("[^0-9.\\-]","");
        try {
            return Double.parseDouble(cleaned);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public double getTotal() {
        return getNumericPrice() * quantity;
    }
}
