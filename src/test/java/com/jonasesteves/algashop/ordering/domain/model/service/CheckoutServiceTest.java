package com.jonasesteves.algashop.ordering.domain.model.service;

import com.jonasesteves.algashop.ordering.domain.model.entity.Order;
import com.jonasesteves.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.entity.PaymentMethod;
import com.jonasesteves.algashop.ordering.domain.model.entity.ProductTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCartTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.exception.ShoppingCartCantProceedToCheckoutException;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Billing;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Shipping;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CheckoutServiceTest {

    private final CheckoutService checkoutService = new CheckoutService();

    @Test
    void shouldGenerateValidOrder() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someEmptyShoppingCart();
        shoppingCart.addItem(ProductTestDataBuilder.someProduct().build(), new Quantity(2));
        shoppingCart.addItem(ProductTestDataBuilder.someProductAlt().build(), new Quantity(10));

        Billing billing = OrderTestDataBuilder.someBilling();
        Shipping shipping = OrderTestDataBuilder.someShipping();
        Quantity expectedItemsQuantity = shoppingCart.totalItems();
        Money expectedTotalAmount = shoppingCart.totalAmount().add(shipping.cost());

        Order order = checkoutService.checkout(shoppingCart, billing, shipping, PaymentMethod.CREDIT_CARD);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(order.customerId()).isNotNull(),
                o -> Assertions.assertThat(order.billing()).isNotNull(),
                o -> Assertions.assertThat(order.shipping()).isNotNull(),
                o -> Assertions.assertThat(order.paymentMethod()).isNotNull(),
                o -> Assertions.assertThat(order.items()).hasSize(2),
                o -> Assertions.assertThat(order.totalItems()).isEqualTo(expectedItemsQuantity),
                o -> Assertions.assertThat(order.totalAmount()).isEqualTo(expectedTotalAmount),
                o -> Assertions.assertThat(order.isPlaced()).isTrue()
        );
        Assertions.assertThat(shoppingCart.isEmpty()).isTrue();
        Assertions.assertThat(shoppingCart.totalAmount()).isEqualTo(Money.ZERO);
        Assertions.assertThat(shoppingCart.totalItems()).isEqualTo(Quantity.ZERO);
    }

    @Test
    void givenShoppingCartWithUnavailableItems_whenCheckout_shouldGenerateException() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someEmptyShoppingCart();
        Product product1 = ProductTestDataBuilder.someProduct().build();

        shoppingCart.addItem(product1, new Quantity(2));
        shoppingCart.addItem(ProductTestDataBuilder.someProductAlt().build(), new Quantity(10));
        Product unavailableProduct1 = ProductTestDataBuilder.someProduct().inStock(false).build();

        shoppingCart.refreshItem(unavailableProduct1);

        Billing billing = OrderTestDataBuilder.someBilling();
        Shipping shipping = OrderTestDataBuilder.someShipping();

        Quantity expectedItemsQuantity = shoppingCart.totalItems();
        Money expectedTotalAmount = shoppingCart.totalAmount();

        Assertions.assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class).isThrownBy(
                () -> checkoutService.checkout(shoppingCart, billing, shipping, PaymentMethod.GATEWAY_BALANCE)
        );
        Assertions.assertThat(shoppingCart.isEmpty()).isFalse();
        Assertions.assertThat(shoppingCart.totalAmount()).isEqualTo(expectedTotalAmount);
        Assertions.assertThat(shoppingCart.totalItems()).isEqualTo(expectedItemsQuantity);
    }

    @Test
    void shouldGenerateExceptionWhenCheckoutEmptyShoppingCart() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someEmptyShoppingCart();
        Billing billing = OrderTestDataBuilder.someBilling();
        Shipping shipping = OrderTestDataBuilder.someShipping();

        Assertions.assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class).isThrownBy(
                () -> checkoutService.checkout(shoppingCart, billing, shipping, PaymentMethod.GATEWAY_BALANCE)
        );
        Assertions.assertThat(shoppingCart.isEmpty()).isTrue();
    }
}