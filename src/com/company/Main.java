package com.company;

import com.company.domain.*;
import com.company.helpers.DeliveryCostCalculatorHelper;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ShoppingCart shoppingCart = new ShoppingCart();

        Category acCategory = new Category("Accessories");
        Category shoes = new Category("Shoes");
        Category pants = new Category("Pants");
        Category jeans = new Category("Jeans");
        Category tights = new Category("Tights");

        jeans.setParentCategory(pants);
        tights.setParentCategory(pants);

        Product shoeProduct = new Product("White Sneaker", 250, shoes);
        Product beltProduct = new Product("Brown Belt", 50, acCategory);
        Product jeanProduct = new Product("Mom Jean", 170, jeans);
        Product tightProduct = new Product("Tight", 100, tights);

        Campaign campaignJean = new Campaign(20, DiscountType.RATE, 2, jeans);
        Campaign campaignTight = new Campaign(30, DiscountType.RATE, 1, tights);

        Campaign campaignShoe = new Campaign(10, DiscountType.AMOUNT, 1, shoes);
        Campaign campaignAcc = new Campaign(50, DiscountType.RATE, 3, acCategory);

        Campaign[] listCampaigns = {campaignJean, campaignShoe, campaignAcc, campaignTight};

        Coupon happyNewYear = new Coupon(50, DiscountType.AMOUNT, 100);
        Coupon winterFest = new Coupon(10, DiscountType.RATE, 150);

        DeliveryCostCalculatorHelper deliveryCostCalculatorHelper = new DeliveryCostCalculatorHelper(5, 3);

        shoppingCart.addProduct(shoeProduct, 1, shoppingCart);
        shoppingCart.addProduct(beltProduct, 3, shoppingCart);
        shoppingCart.addProduct(jeanProduct, 2, shoppingCart);
        shoppingCart.addProduct(tightProduct, 1, shoppingCart);
        HashMap<Product, Integer> products = shoppingCart.getProducts();

        shoppingCart.getTotalAmountAfterDiscounts(winterFest, listCampaigns, shoppingCart);

        shoppingCart.printShoppingCart(shoppingCart, deliveryCostCalculatorHelper);


    }
}
