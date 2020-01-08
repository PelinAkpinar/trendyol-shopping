package com.company.domain;

import com.company.helpers.CampaignHelper;
import com.company.helpers.CategoryHelper;
import com.company.helpers.DeliveryCostCalculatorHelper;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ShoppingCart {

    private HashMap products = new HashMap();
    private double totalAmount;
    private double rawAmount;

    public HashMap getProducts() {
        return products;
    }

    public void setProducts(HashMap products) {
        this.products = products;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getRawAmount() {
        return rawAmount;
    }

    public void setRawAmount(double rawAmount) {
        this.rawAmount = rawAmount;
    }

    public void addProduct(Product product, Integer productQuantity, ShoppingCart shoppingCart) {
        HashMap<Product, Integer> productMap = shoppingCart.getProducts();
        productMap.put(product, productQuantity);

        shoppingCart.setRawAmount(shoppingCart.getRawAmount() + product.getPrice() * productMap.get(product));

        shoppingCart.setProducts(productMap);

    }

    public void calculateTotalAmountOfProducts(ShoppingCart shoppingCart) {

        double totalAmount = 0.0;
        HashMap<Product, Integer> productMap = shoppingCart.getProducts();

        for (Product product : productMap.keySet()) {
            for (int i = 0; i < productMap.get(product); i++) {
                totalAmount = totalAmount + product.getPrice();
            }
        }


        shoppingCart.setTotalAmount(totalAmount);
    }

    public double getDeliveryCost(ShoppingCart shoppingCart, DeliveryCostCalculatorHelper deliveryCostCalculatorHelper) {

        return deliveryCostCalculatorHelper.calculateFor(shoppingCart);
    }

    public double getCouponDiscount(Coupon coupon, ShoppingCart shoppingCart) {

        double couponDiscount;

        if (shoppingCart.getTotalAmount() >= coupon.getMinAmount()) {
            if (coupon.getDiscountType() == DiscountType.AMOUNT) {
                couponDiscount = coupon.getDiscountDecimal();
            } else {
                couponDiscount = (shoppingCart.getTotalAmount() * coupon.getDiscountDecimal() / 100);
            }
            return couponDiscount;
        } else {
            return 0;
        }
    }

    public double applyCoupon(Coupon coupon, ShoppingCart shoppingCart) {
        double couponDiscount = getCouponDiscount(coupon, shoppingCart);

        shoppingCart.setTotalAmount(shoppingCart.getTotalAmount() - couponDiscount);

        return shoppingCart.getTotalAmount();
    }

    public double getCampaignDiscount(Campaign campaign, ShoppingCart shoppingCart) {
        double campaignDiscount = 0;
        CampaignHelper campaignHelper = new CampaignHelper();

        if (!campaignHelper.validateCampaign(campaign, shoppingCart))
            return 0;

        HashMap<Product, Integer> cartProducts = shoppingCart.getProducts();

        if (campaign.getDiscountType() == DiscountType.AMOUNT) {
            for (Product product : cartProducts.keySet()) {
                if (product.getCategory().getTitle().equals(campaign.getCategory().getTitle())) {
                    if (product.getPrice() > campaign.getDiscountDecimal()) {
                        campaignDiscount += campaign.getDiscountDecimal();

                    } else {
                        campaignDiscount += product.getPrice();
                    }
                }

            }

        }
        if (campaign.getDiscountType() == DiscountType.RATE) {
            for (Product product : cartProducts.keySet()) {
                if (product.getCategory().getTitle().equals(campaign.getCategory().getTitle())) {
                    campaignDiscount += product.getPrice() * campaign.getDiscountDecimal() / 100;
                }
            }
        }

        return campaignDiscount;
    }

    public void applyCampaign(Campaign campaign, ShoppingCart shoppingCart) {
        HashMap<Product, Integer> cartProducts = shoppingCart.getProducts();


        if (campaign.getDiscountType() == DiscountType.AMOUNT) {
            for (Product product : cartProducts.keySet()) {
                if (product.getCategory().getTitle().equals(campaign.getCategory().getTitle())) {
                    if (product.getPrice() > campaign.getDiscountDecimal()) {
                        product.setPrice(product.getPrice() - campaign.getDiscountDecimal());
                    } else {
                        product.setPrice(0);
                    }
                }
            }
        } else {
            for (Product product : cartProducts.keySet()) {
                if (product.getCategory().getTitle().equals(campaign.getCategory().getTitle())) {
                    product.setPrice(product.getPrice() - (product.getPrice() * campaign.getDiscountDecimal() / 100));

                }
            }
        }

    }

    public double applyDiscounts(Campaign[] camps, ShoppingCart shoppingCart) {

        CategoryHelper categoryHelper = new CategoryHelper();
        CampaignHelper campaignHelper = new CampaignHelper();

        Set<Product> products = shoppingCart.getProducts().keySet();
        List<Category> distinctCategories = categoryHelper.getDistinctCategories(products);
        for(Category category : distinctCategories){
            System.out.println("Distinct:" + category.getTitle());
        }
        double maxDiscount = 0;
        double campaignDiscount;
        Campaign toApplied = null;
        List<Campaign> filteredCampaigns;
        for (Category category : distinctCategories) {
            filteredCampaigns = campaignHelper.campaignFilter(camps, category, shoppingCart);


            for (Campaign campaign : filteredCampaigns) {
                campaignDiscount = getCampaignDiscount(campaign, shoppingCart);
                if (maxDiscount < campaignDiscount) {
                    maxDiscount = campaignDiscount;

                    toApplied = campaign;
                }

            }
            if (toApplied != null) {
                applyCampaign(toApplied, shoppingCart);
                System.out.println("Applied: " + toApplied.getDiscountDecimal() + " " + toApplied.getCategory().getTitle());
                toApplied = null;
            }
            maxDiscount = 0;
        }
        calculateTotalAmountOfProducts(shoppingCart);
        return shoppingCart.getTotalAmount();
    }

    public double getTotalAmountAfterDiscounts(Coupon coupon, Campaign[] campaigns, ShoppingCart shoppingCart) {
        calculateTotalAmountOfProducts(shoppingCart);
        double totalAmountAfterCampaigns = applyDiscounts(campaigns, shoppingCart);
        shoppingCart.setTotalAmount(totalAmountAfterCampaigns);

        double totalAmountAfterCoupons = applyCoupon(coupon, shoppingCart);
        shoppingCart.setTotalAmount(totalAmountAfterCoupons);


        return shoppingCart.getTotalAmount();
    }

    public void printShoppingCart(ShoppingCart shoppingCart, DeliveryCostCalculatorHelper deliveryCostCalculatorHelper) {
        List<Category> distinctCategories;
        CategoryHelper categoryHelper = new CategoryHelper();
        DecimalFormat digitFormat = new DecimalFormat("#.00");
        HashMap<Product, Integer> products = shoppingCart.getProducts();
        System.out.println("SHOPPING CART INFORMATION");
        System.out.println("CART PRODUCTS");
        System.out.println("----------------------------------");

        distinctCategories = categoryHelper.getDistinctCategories(products.keySet());
        for (Category category : distinctCategories) {
            if (category.getParentCategory() != null) {
                System.out.println("Parent Category: " + category.getParentCategory().getTitle());
            }
            System.out.println(category.getTitle() + " Category");
            for (Product product : products.keySet()) {
                if (product.getCategory().getTitle().equals(category.getTitle())) {

                    System.out.println("Title: " + product.getTitle());
                    System.out.println("Price: " + product.getRawPrice());
                    System.out.println("Quantity: " + products.get(product));
                    System.out.println("----------------------------------");
                }
            }
            System.out.println("----------------------------------");
        }

        System.out.println("Total Amount: " + shoppingCart.getRawAmount());

        System.out.println("Total Amount After Discounts: " + shoppingCart.getTotalAmount());

        System.out.println("Delivery Cost: " + digitFormat.format(shoppingCart.getDeliveryCost(shoppingCart, deliveryCostCalculatorHelper)));

    }


}
