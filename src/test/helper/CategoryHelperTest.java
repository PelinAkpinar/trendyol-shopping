package test.helper;

import com.company.domain.Category;
import com.company.domain.Product;
import com.company.domain.ShoppingCart;
import com.company.helpers.CategoryHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CategoryHelperTest {
    Category testCategory1 = new Category("Food");
    Category testCategory2 = new Category("Clothes");

    Product testProduct = new Product("Yoghurt",5, testCategory1);
    Product testProduct2 = new Product("Pickle",20, testCategory1);
    Product testProduct3 = new Product("Sweatshirt",70, testCategory2);

    ShoppingCart testShoppingCart = new ShoppingCart();
    CategoryHelper categoryHelper = new CategoryHelper();
    List<Category> testDistinctCategories;

    @Test
    public void it_should_get_distinct_categories_for_given_cart_products(){
        //set
        testShoppingCart.addProduct(testProduct,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct2,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct3,1,testShoppingCart);

        //act
        testDistinctCategories = categoryHelper.getDistinctCategories(testShoppingCart.getProducts().keySet());

        //assert
        Assertions.assertEquals(2,testDistinctCategories.size());
    }

}
