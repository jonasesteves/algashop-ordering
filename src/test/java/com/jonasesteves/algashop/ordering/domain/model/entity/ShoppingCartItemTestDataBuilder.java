package com.jonasesteves.algashop.ordering.domain.model.entity;

import com.jonasesteves.algashop.ordering.domain.model.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;

public class ShoppingCartItemTestDataBuilder {

//    private ShoppingCartId shoppingCartId = ShoppingCartTestDataBuilder.DEFAULT_SHOPPING_CART_ID;
//    private ProductId productId = ProductTestDataBuilder.DEFAULT_PRODUCT_ID;
//    private ProductName productName = new ProductName("Notebook");
//    private Money price = new Money("1000");
//    private Quantity quantity = new Quantity(1);
//    private boolean available = true;

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

    public static ShoppingCartItem.BrandNewShoppingCartItemBuilder someOtherShoppingCartItem() {
        Product product = ProductTestDataBuilder.someProductAlt().build();
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
