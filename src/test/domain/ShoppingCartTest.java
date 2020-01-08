package test.domain;

import com.company.domain.*;
import com.company.helpers.DeliveryCostCalculatorHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class ShoppingCartTest {

    Category testCategory1 = new Category("Food");
    Category testCategory2 = new Category("Clothes");

    //products that belong to two distinct categories
    Product testProduct = new Product("Yoghurt",5, testCategory1);
    Product testProduct2 = new Product("Pickle",20, testCategory1);
    Product testProduct3 = new Product("Sweatshirt",70, testCategory2);

    HashMap<Product, Integer> testProductMap = new HashMap<>();

    ShoppingCart testShoppingCart = new ShoppingCart();

    //campaigns that have to apply to two distinct categories
    //campaigns in same category: one campaign that applies more discount have to be selected
    Campaign testCampaign1 = new Campaign( 50,DiscountType.RATE,2, testCategory1);
    Campaign testCampaign2 = new Campaign(10, DiscountType.AMOUNT,1, testCategory2);
    Campaign testCampaign3 = new Campaign(20, DiscountType.RATE,2, testCategory1);

    Coupon testCoupon1 = new Coupon(10,DiscountType.RATE,50);
    Coupon testCoupon2 = new Coupon(20,DiscountType.AMOUNT,40);
    Coupon testCoupon3 = new Coupon(200,DiscountType.AMOUNT,1000);

    @Test
    void it_should_add_product_with_quantity_to_cart() {
        //set
        testProductMap.put(testProduct,1);
        testShoppingCart.setProducts(testProductMap);

        //act
        testShoppingCart.addProduct(testProduct,1,testShoppingCart);

        //assert
        Assertions.assertTrue(testShoppingCart.getProducts().containsKey(testProduct));
    }

    @Test
    void it_should_applyDiscounts_each_distinct_categories_with_maximum_discount_amount() {
        //set
        Campaign[] testCampaigns ={testCampaign1,testCampaign2,testCampaign3};
        testShoppingCart.setProducts(testProductMap);
        testShoppingCart.addProduct(testProduct,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct2,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct3,1,testShoppingCart);

        testShoppingCart.calculateTotalAmountOfProducts(testShoppingCart);

        //act
        double amount = testShoppingCart.applyDiscounts(testCampaigns, testShoppingCart);

        System.out.println(testShoppingCart.getProducts().values());

        System.out.println(testShoppingCart.getProducts().size());

        //assert
        //effective discounts have to be selected in each category so:
        // %50 on food, 10 unit on clothes 25/2 + 70-10 = 72.5 expected
        Assertions.assertEquals(72.5,amount);

    }

    @Test
    void it_should_calculate_and_set_the_amount_of_products_in_cart_as_price() {
        //set
        testShoppingCart.addProduct(testProduct,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct2,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct3,1,testShoppingCart);

        //act
        testShoppingCart.calculateTotalAmountOfProducts(testShoppingCart);

        //assert
        // product total (95) is expected
        Assertions.assertEquals(95,testShoppingCart.getTotalAmount());
    }

    @Test
    void it_should_calculate_and_get_delivery_cost_dynamically() {
        //set
        testShoppingCart.addProduct(testProduct,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct2,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct3,1,testShoppingCart);
        testShoppingCart.calculateTotalAmountOfProducts(testShoppingCart);

        DeliveryCostCalculatorHelper deliveryCostCalculatorHelper = new DeliveryCostCalculatorHelper(10,5);

        //act
        double testDeliveryCost = testShoppingCart.getDeliveryCost(testShoppingCart,deliveryCostCalculatorHelper);

        //assert
        // 2 distinct categories, 3 products, costPerProduct is 5, costPerDelivery is 10 so:
        // 10*2 + 5*3 + 2.99 = 37.99 expected
        Assertions.assertEquals(37.99,testDeliveryCost);
    }

    @Test
    void it_shouldnt_change_total_amount_when_applied_coupon_min_limit_is_over() {
        //set
        testShoppingCart.addProduct(testProduct,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct2,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct3,1,testShoppingCart);
        testShoppingCart.calculateTotalAmountOfProducts(testShoppingCart);

        //act
        double discount= testShoppingCart.applyCoupon(testCoupon3,testShoppingCart);

        //assert
        //if minimum amount is much than cart amount then return raw amount
        // (95 expected as product total)
        Assertions.assertEquals(95,discount);

    }

    @Test
    void it_should_get_discount_amount_after_given_campaign() {
        //set
        testShoppingCart.addProduct(testProduct,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct2,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct3,1,testShoppingCart);
        testShoppingCart.calculateTotalAmountOfProducts(testShoppingCart);
        //act
        double campaignDiscount = testShoppingCart.getCampaignDiscount(testCampaign1,testShoppingCart);

        //assert
        //%50 discount in food category campaign 25/2 = 12.5 expected
        Assertions.assertEquals(12.5, campaignDiscount);

    }

    @Test
    void it_should_apply_campaign_on_campaign_category_products_price_in_cart() {
        //set
        testShoppingCart.addProduct(testProduct,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct2,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct3,1,testShoppingCart);
        testShoppingCart.calculateTotalAmountOfProducts(testShoppingCart);

        //act
        testShoppingCart.applyCampaign(testCampaign3,testShoppingCart);

        //assert
        //%20 on food category, in cart 2 products in food category
        //expected 20/5 = 4 -> 20-4 = 16 for pickle and 5/5= 1 -> 5-1 = 4 for yoghurt
        Assertions.assertEquals(4,testProduct.getPrice());
        Assertions.assertEquals(16,testProduct2.getPrice());
    }

    @Test
    void getTotalAmountAfterDiscounts() {
        //set
        Campaign[] testCampaigns ={testCampaign1,testCampaign2,testCampaign3};
        testShoppingCart.addProduct(testProduct,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct2,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct3,1,testShoppingCart);

        //act
        double amount = testShoppingCart.getTotalAmountAfterDiscounts(testCoupon1,testCampaigns,testShoppingCart);

        //assert
        //First campaigns then coupon has to be applied
        //Effective campaign selected as applyDiscounts method do 72.5 is expected
        //Coupon discount is %10 of total amount after selected campaigns applied 72.5-7.25 = 65.25 expected at last
        Assertions.assertEquals(65.25,amount);
    }

    @Test
    void printCart(){
        Campaign[] testCampaigns ={testCampaign1,testCampaign2,testCampaign3};

        testShoppingCart.addProduct(testProduct,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct2,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct3,1,testShoppingCart);

        DeliveryCostCalculatorHelper deliveryCostCalculatorHelper = new DeliveryCostCalculatorHelper(10,5);
        testShoppingCart.getTotalAmountAfterDiscounts(testCoupon1,testCampaigns,testShoppingCart);

        testShoppingCart.printShoppingCart(testShoppingCart, deliveryCostCalculatorHelper);
    }
}
