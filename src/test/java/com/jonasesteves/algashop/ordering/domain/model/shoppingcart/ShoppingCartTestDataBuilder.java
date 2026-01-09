package com.jonasesteves.algashop.ordering.domain.model.shoppingcart;

import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.product.Product;
import com.jonasesteves.algashop.ordering.domain.model.commons.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductTestDataBuilder;

public class ShoppingCartTestDataBuilder {
    public static CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;
    public static final ShoppingCartId DEFAULT_SHOPPING_CART_ID = new ShoppingCartId();

    public static ShoppingCart someEmptyShoppingCart() {
        return ShoppingCart.startShopping(customerId);
    }

    public static ShoppingCart someShoppingCartWithProduct() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product newProduct1 = ProductTestDataBuilder.someProduct().build();

        shoppingCart.addItem(newProduct1, new Quantity(2));

        return  shoppingCart;
    }

    public static ShoppingCart someShoppingCartWithTwoProducts() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product newProduct1 = ProductTestDataBuilder.someProduct().build();
        Product newProduct2 = ProductTestDataBuilder.someProductAlt().build();

        shoppingCart.addItem(newProduct1, new Quantity(2));
        shoppingCart.addItem(newProduct2, new Quantity(3));

        return  shoppingCart;
    }


}
