package com.company.domain;

public class Discount {

    private int discountDecimal;
    private DiscountType discountType;

    public Discount(int discountDecimal, DiscountType discountType) {
        this.discountDecimal = discountDecimal;
        this.discountType = discountType;
    }

    public int getDiscountDecimal() {
        return discountDecimal;
    }

    public void setDiscountDecimal(int discountDecimal) {
        this.discountDecimal = discountDecimal;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }
}
