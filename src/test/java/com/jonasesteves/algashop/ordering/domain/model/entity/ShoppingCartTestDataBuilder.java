package com.jonasesteves.algashop.ordering.domain.model.entity;

import com.jonasesteves.algashop.ordering.domain.model.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;

public class ShoppingCartTestDataBuilder {

    public static final ShoppingCartId DEFAULT_SHOPPING_CART_ID = new ShoppingCartId();

    public static ShoppingCart someShoppingCartWithProduct() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product newProduct1 = ProductTestDataBuilder.someProduct().build();

        shoppingCart.addItem(newProduct1, new Quantity(2));

        return  shoppingCart;
    }

    public static ShoppingCart someShoppingCartWithTwoProducts() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product newProduct1 = ProductTestDataBuilder.someProduct().build();
        Product newProduct2 = ProductTestDataBuilder.chocolate().build();

        shoppingCart.addItem(newProduct1, new Quantity(2));
        shoppingCart.addItem(newProduct2, new Quantity(3));

        return  shoppingCart;
    }


}
