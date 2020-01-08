package test.helper;

import com.company.domain.*;
import com.company.helpers.CampaignHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CampaignHelperTest {
    Category testCategory1 = new Category("Food");
    Category testCategory2 = new Category("Clothes");

    Product testProduct = new Product("Yoghurt",5, testCategory1);
    Product testProduct2 = new Product("Pickle",20, testCategory1);
    Product testProduct3 = new Product("Sweatshirt",70, testCategory2);

    Campaign testCampaign1 = new Campaign( 50, DiscountType.RATE,2, testCategory1);
    Campaign testCampaign2 = new Campaign(10, DiscountType.AMOUNT,1, testCategory2);
    Campaign testCampaign3 = new Campaign(20, DiscountType.RATE,3, testCategory1);

    Campaign[] testCampaigns ={testCampaign1,testCampaign2,testCampaign3};

    CampaignHelper campaignHelper = new CampaignHelper();
    ShoppingCart testShoppingCart = new ShoppingCart();

    @Test
    void it_should_filter_campaigns_in_a_specific_category_that_suitable_for_min_quantity_with_cart_products(){
        //set
        testShoppingCart.addProduct(testProduct,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct2,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct3,1,testShoppingCart);

        //act
        List<Campaign> filteredCampaigns = campaignHelper.campaignFilter(testCampaigns,testCategory1,testShoppingCart);

        //assert
        //only one of campaigns suitable for min quantity of products in Food category, expected size = 1
        Assertions.assertEquals(1,filteredCampaigns.size());
    }

    @Test
    void it_should_validate_campaigns_that_proper_for_product_quantity_limit(){
        //set
        testShoppingCart.addProduct(testProduct,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct2,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct3,1,testShoppingCart);

        //act
        boolean isValidated= campaignHelper.validateCampaign(testCampaign1,testShoppingCart);

        //assert
        Assertions.assertTrue(isValidated);
    }
}
