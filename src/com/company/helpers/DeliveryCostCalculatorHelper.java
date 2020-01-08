package com.company.helpers;

import com.company.domain.Category;
import com.company.domain.Product;
import com.company.domain.ShoppingCart;

import java.util.List;
import java.util.Set;

public class DeliveryCostCalculatorHelper {
    private double costPerDelivery;
    private double costPerProduct;
    private final double fixedCost = 2.99;

    public DeliveryCostCalculatorHelper(double costPerDelivery, double costPerProduct) {
        this.costPerDelivery = costPerDelivery;
        this.costPerProduct = costPerProduct;
    }

    public double calculateFor(ShoppingCart shoppingCart) {

        CategoryHelper categoryHelper = new CategoryHelper();
        Set<Product> cartProducts = shoppingCart.getProducts().keySet();
        List<Category> deliveryList = categoryHelper.getDistinctCategories(cartProducts);

        int numberOfDeliveries = deliveryList.size();
        int numberOfProducts = cartProducts.size();

        double deliveryCost = (costPerDelivery * numberOfDeliveries) + (costPerProduct * numberOfProducts) + fixedCost;
        return deliveryCost;
    }

    public double getCostPerDelivery() {
        return costPerDelivery;
    }

    public void setCostPerDelivery(double costPerDelivery) {
        this.costPerDelivery = costPerDelivery;
    }

    public double getCostPerProduct() {
        return costPerProduct;
    }

    public void setCostPerProduct(double costPerProduct) {
        this.costPerProduct = costPerProduct;
    }
}

