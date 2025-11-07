package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ShoppingCartId;

public class ShoppingCartItemTestDataBuilder {

    private ShoppingCartItemTestDataBuilder() {
    }

    public static ShoppingCartItem.BrandNewShoppingCartItemBuilder someShoppingCartItem() {
        Product product = ProductTestDataBuilder.someProduct().build();
        Quantity quantity = new Quantity(2);
        ShoppingCartId shoppingCartId = new ShoppingCartId();

        return ShoppingCartItem.brandNew()
                .shoppingCartId(shoppingCartId)
                .productId(product.id())
                .productName(product.name())
                .price(product.price())
                .quantity(quantity)
                .available(product.inStock());
    }

    public static ShoppingCartItem.BrandNewShoppingCartItemBuilder someUnavailableShoppingCartItem() {
        Product product = ProductTestDataBuilder.someUnavailableProduct().build();
        Quantity quantity = new Quantity(2);
        ShoppingCartId shoppingCartId = new ShoppingCartId();

        return ShoppingCartItem.brandNew()
                .shoppingCartId(shoppingCartId)
                .productId(product.id())
                .productName(product.name())
                .price(product.price())
                .quantity(quantity)
                .available(product.inStock());
    }
}
