package com.company.helpers;

import com.company.domain.Campaign;
import com.company.domain.Category;
import com.company.domain.Product;
import com.company.domain.ShoppingCart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CampaignHelper {
    public List<Campaign> campaignFilter(Campaign[] campaigns, Category category, ShoppingCart shoppingCart) {
        List<Campaign> filteredCampaigns = new ArrayList<>();
        HashMap<Product, Integer> products = shoppingCart.getProducts();
        int quantity = 0;

        for (Campaign campaign : campaigns) {
            for (Product product : products.keySet()) {
                if (product.getCategory().getTitle().equals(campaign.getCategory().getTitle())) {
                    quantity += products.get(product);
                }

            }
            if (campaign.getCategory().getTitle().equals(category.getTitle()) && quantity >= campaign.getProductQuantityLimit()) {
                filteredCampaigns.add(campaign);
            }

            quantity = 0;
        }

        for(Campaign campaign : filteredCampaigns){
            System.out.println("Filtered:" + campaign.getCategory().getTitle());
        }
        return filteredCampaigns;
    }

    public boolean validateCampaign(Campaign campaign, ShoppingCart shoppingCart) {
        HashMap<Product, Integer> cartProducts = shoppingCart.getProducts();

        int campaignCategoryCount = 0;
        for (Product product : cartProducts.keySet()) {
            if (product.getCategory().getTitle().equals(campaign.getCategory().getTitle())) {
                campaignCategoryCount += cartProducts.get(product);
            }
        }

        return campaignCategoryCount >= campaign.getProductQuantityLimit();

    }
}
