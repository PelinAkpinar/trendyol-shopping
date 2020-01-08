package test.helper;

import com.company.domain.Category;
import com.company.domain.Product;
import com.company.domain.ShoppingCart;
import com.company.helpers.DeliveryCostCalculatorHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

public class DeliveryCostCalculatorHelperTest {

    Category testCategory1 = new Category("Food");
    Category testCategory2 = new Category("Clothes");

    Product testProduct = new Product("Yoghurt",5, testCategory1);
    Product testProduct2 = new Product("Pickle",20, testCategory1);
    Product testProduct3 = new Product("Sweatshirt",70, testCategory2);

    ShoppingCart testShoppingCart = new ShoppingCart();
    DeliveryCostCalculatorHelper deliveryCostCalculatorHelper = new DeliveryCostCalculatorHelper(5,5);



    @Test
    public void it_should_calculate_delivery_cost_for_given_shopping_cart(){
        //set
        testShoppingCart.addProduct(testProduct,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct2,1,testShoppingCart);
        testShoppingCart.addProduct(testProduct3,1,testShoppingCart);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        //act
        double deliveryCost = deliveryCostCalculatorHelper.calculateFor(testShoppingCart);

        //assert
        Assertions.assertEquals("27.99", decimalFormat.format(deliveryCost));
    }
}
