package com.company.domain;

public class Campaign extends Discount{

    private int productQuantityLimit;
    private Category category;

    public Campaign(int discount, DiscountType discountType, int productQuantityLimit, Category category) {
        super(discount,discountType);
        this.productQuantityLimit = productQuantityLimit;
        this.category = category;
    }

    public int getProductQuantityLimit() {
        return productQuantityLimit;
    }

    public void setProductQuantityLimit(int productQuantityLimit) {
        this.productQuantityLimit = productQuantityLimit;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
