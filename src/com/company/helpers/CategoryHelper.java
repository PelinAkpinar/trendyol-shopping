package com.company.helpers;

import com.company.domain.Category;
import com.company.domain.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryHelper {
    public List<Category> getDistinctCategories(Set<Product> products) {
        List<Category> distinctCategories = new ArrayList<>();

        for (Product product : products) {
            if (!distinctCategories.contains(product.getCategory())) {
                distinctCategories.add(product.getCategory());
            }
        }
        return distinctCategories;
    }
}
