package com.company.domain;

public class Product {

    private String title;
    private double price;
    private double rawPrice;
    private Category category;

    public Product(String title, double price, Category category) {
        this.title = title;
        this.price = price;
        this.category = category;
        rawPrice = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getRawPrice() {
        return rawPrice;
    }

    public void setRawPrice(double rawPrice) {
        this.rawPrice = rawPrice;
    }
}
