package com.company.domain;

public class Coupon extends Discount {

    private double minAmount;

    public Coupon(int discount, DiscountType discountType, double minAmount) {
        super(discount,discountType);
        this.minAmount = minAmount;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }
}
