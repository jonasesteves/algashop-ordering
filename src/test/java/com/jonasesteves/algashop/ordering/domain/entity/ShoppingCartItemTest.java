package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.exception.ShoppingCartItemIncompatibleProductException;
import com.jonasesteves.algashop.ordering.domain.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.valueobject.ProductName;
import com.jonasesteves.algashop.ordering.domain.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ProductId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ShoppingCartItemTest {

    @Test
    void shouldGenerateBrandNewShoppingCartItem() {
        Product product = ProductTestDataBuilder.someProduct().build();
        Quantity quantity = new Quantity(2);
        ShoppingCartId shoppingCartId = new ShoppingCartId();

        ShoppingCartItem shoppingCartItem = ShoppingCartItemTestDataBuilder.someShoppingCartItem().build();

        Assertions.assertWith(shoppingCartItem,
                s -> Assertions.assertThat(s.id()).isNotNull(),
                s -> Assertions.assertThat(s.shoppingCartId()).isEqualTo(shoppingCartId),
                s -> Assertions.assertThat(s.productId()).isEqualTo(product.id()),
                s -> Assertions.assertThat(s.name()).isEqualTo(product.name()),
                s -> Assertions.assertThat(s.price()).isEqualTo(product.price()),
                s -> Assertions.assertThat(s.quantity()).isEqualTo(quantity),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(new Money("6000.00")),
                s -> Assertions.assertThat(s.available()).isTrue()
        );
    }

    @Test
    void shouldChangeQuantityCorrectly() {
        ShoppingCartItem shoppingCartItem = ShoppingCartItemTestDataBuilder.someShoppingCartItem().build();

        shoppingCartItem.changeQuantity(new Quantity(4));

        Assertions.assertThat(shoppingCartItem.quantity()).isEqualTo(new Quantity(4));
        Assertions.assertThat(shoppingCartItem.totalAmount()).isEqualTo(new Money("12000.00"));
    }

    @Test
    void givenShoppingCartItem_whenChangeQuantityToZero_shouldGenerateException() {
        ShoppingCartItem shoppingCartItem = ShoppingCartItemTestDataBuilder.someShoppingCartItem().build();

        Quantity quantityZero = new Quantity(0);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> shoppingCartItem.changeQuantity(quantityZero));
        Assertions.assertWith(shoppingCartItem,
                s -> Assertions.assertThat(s.quantity()).isEqualTo(new Quantity(2)),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(new Money("6000.00"))
        );
    }

    @Test
    void givenShoppingCartItem_whenRefreshProductData_shouldRefreshCorrectly() {
        ShoppingCartItem shoppingCartItem = ShoppingCartItemTestDataBuilder.someShoppingCartItem().build();
        ProductId productId = shoppingCartItem.productId();

        Product newProductData = new Product(
                productId,
                new ProductName("Other Name"),
                new Money("2.50"),
                true
        );

        shoppingCartItem.refresh(newProductData);

        Assertions.assertWith(shoppingCartItem,
                s -> Assertions.assertThat(s.name()).isEqualTo(newProductData.name()),
                s -> Assertions.assertThat(s.price()).isEqualTo(newProductData.price()),
                s -> Assertions.assertThat(s.available()).isEqualTo(newProductData.inStock()),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(new Money("5.00"))
        );
    }

    @Test
    void givenShoppingCartItem_whenRefreshProductDataWithDifferentProduct_shouldGenerateException() {
        ShoppingCartItem shoppingCartItem = ShoppingCartItemTestDataBuilder.someShoppingCartItem().build();
        Product cartItem = new Product(
                shoppingCartItem.productId(),
                shoppingCartItem.name(),
                shoppingCartItem.price(),
                shoppingCartItem.available()
        );
        Product product = ProductTestDataBuilder.someProduct().build();

        Assertions.assertThatExceptionOfType(ShoppingCartItemIncompatibleProductException.class)
                .isThrownBy(() -> shoppingCartItem.refresh(product));

        Assertions.assertWith(shoppingCartItem,
                s -> Assertions.assertThat(s.name()).isEqualTo(cartItem.name()),
                s -> Assertions.assertThat(s.price()).isEqualTo(cartItem.price()),
                s -> Assertions.assertThat(s.available()).isEqualTo(cartItem.inStock()),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(new Money("6000.00"))
        );
    }
}