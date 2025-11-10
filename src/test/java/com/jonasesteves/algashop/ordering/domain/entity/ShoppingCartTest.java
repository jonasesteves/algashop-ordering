package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.exception.ShoppingCartDoesNotContainItemException;
import com.jonasesteves.algashop.ordering.domain.exception.ShoppingCartDoesNotContainProductException;
import com.jonasesteves.algashop.ordering.domain.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.valueobject.ProductName;
import com.jonasesteves.algashop.ordering.domain.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ProductId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ShoppingCartTest {

    @Test
    void shouldGenerateEmptyCart() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Assertions.assertWith(shoppingCart,
                s -> Assertions.assertThat(s.id()).isNotNull(),
                s -> Assertions.assertThat(s.customerId()).isEqualTo(customerId),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(Money.ZERO),
                s -> Assertions.assertThat(s.totalItems()).isEqualTo(Quantity.ZERO),
                s -> Assertions.assertThat(s.createdAt()).isNotNull()
        );
    }

    @Test
    void shouldAddItemsToShoppingCartCorrectly() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithTwoProducts();

        Assertions.assertWith(shoppingCart,
                s -> Assertions.assertThat(s.items()).hasSize(2),
                s -> Assertions.assertThat(s.totalItems()).isEqualTo(new Quantity(5)),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(new Money("6002.97"))
        );
    }

    @Test
    void shouldEmptyShoppingCart() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();
        shoppingCart.empty();

        Assertions.assertWith(shoppingCart,
                s -> Assertions.assertThat(s.items()).isEmpty(),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(Money.ZERO),
                s -> Assertions.assertThat(s.totalItems()).isEqualTo(Quantity.ZERO)
        );
    }

    @Test
    void givenShoppingCartWithItems_whenRemoveItem_shouldRemoveSuccessfully() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();
        ShoppingCartItem shoppingCartItem = shoppingCart.items().iterator().next();

        Product product = ProductTestDataBuilder.chocolate().build();
        shoppingCart.addItem(product, new Quantity(1));

        shoppingCart.removeItem(shoppingCartItem.id());

        Assertions.assertWith(shoppingCart,
                s -> Assertions.assertThat(s.items()).hasSize(1),
                s -> Assertions.assertThat(s.totalItems()).isEqualTo(new Quantity(1)),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(new Money("0.99"))
        );
    }

    @Test
    void givenShoppingCartWithItems_whenRemoveInexistentItem_shouldGenerateException() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();

        ShoppingCartItemId shoppingCartItemId = new ShoppingCartItemId();
        Assertions.assertThatExceptionOfType(ShoppingCartDoesNotContainItemException.class)
                .isThrownBy(() -> shoppingCart.removeItem(shoppingCartItemId));

        Assertions.assertWith(shoppingCart,
                s -> Assertions.assertThat(s.items()).hasSize(1),
                s -> Assertions.assertThat(s.totalItems()).isEqualTo(new Quantity(2)),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(new Money("6000.00"))
        );
    }

    @Test
    void givenShoppingCartWithItems_whenRefreshItem_shouldRefreshCorrectly() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();
        ShoppingCartItem shoppingCartItem = shoppingCart.items().iterator().next();
        Product product = Product.builder()
                .id(shoppingCartItem.productId())
                .name(new ProductName("New Product Name"))
                .price(new Money("100.00"))
                .inStock(true)
                .build();

        shoppingCart.refreshItem(product);

        Assertions.assertWith(shoppingCart,
                s -> Assertions.assertThat(s.items()).hasSize(1),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(new Money("200.00")),
                s -> Assertions.assertThat(s.items().iterator().next().name()).isEqualTo(product.name())
        );
    }

    @Test
    void givenShoppingCartWithItems_whenRefreshUnexistingItem_shouldGenerateExcaption() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();
        Product product = Product.builder()
                .id(new ProductId())
                .name(new ProductName("New Product Name"))
                .price(new Money("100.00"))
                .inStock(true)
                .build();

        Assertions.assertThatExceptionOfType(ShoppingCartDoesNotContainProductException.class)
                .isThrownBy(() -> shoppingCart.refreshItem(product));

        Assertions.assertWith(shoppingCart,
                s -> Assertions.assertThat(s.items()).hasSize(1),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(new Money("6000.00")),
                s -> Assertions.assertThat(s.items().iterator().next().name()).isEqualTo(new ProductName("Adamantium"))
        );
    }

    @Test
    void givenShoppingCartWithItems_whenChangeItemQuantity_shouldChangeCorrectly() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();
        ShoppingCartItem shoppingCartItem = shoppingCart.items().iterator().next();

        shoppingCart.changeItemQuantity(shoppingCartItem.id(), new Quantity(4));

        Assertions.assertThat(shoppingCartItem.quantity()).isEqualTo(new Quantity(4));
        Assertions.assertThat(shoppingCart.totalAmount()).isEqualTo(new Money("12000.00"));
        Assertions.assertThat(shoppingCart.totalItems()).isEqualTo(new Quantity(4));
    }

    @Test
    void shouldIdentifyIfSomeProductIsUnavailable() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();
        ShoppingCartItem shoppingCartItem = shoppingCart.items().iterator().next();
        Product product = ProductTestDataBuilder.someProduct()
                .id(shoppingCartItem.productId())
                .inStock(false)
                .build();

        Assertions.assertThat(shoppingCart.containsUnavailableItems()).isFalse();

        shoppingCart.refreshItem(product);

        Assertions.assertThat(shoppingCart.containsUnavailableItems()).isTrue();
    }
}