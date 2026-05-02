package com.jonasesteves.algashop.ordering.domain.model.order;

import com.jonasesteves.algashop.ordering.domain.model.commons.Money;
import com.jonasesteves.algashop.ordering.domain.model.commons.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customer;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.jonasesteves.algashop.ordering.domain.model.product.Product;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    private CheckoutService checkoutService;

    @Mock
    private Orders orders;

    @BeforeEach
    void setup() {
        var specification = new CustomerHasFreeShippingSpecification(
                orders,
                new LoyaltyPoints(100),
                2L,
                new LoyaltyPoints(2000)
        );
        checkoutService = new CheckoutService(specification);
    }

    @Test
    void shouldGenerateValidOrder() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someEmptyShoppingCart();
        shoppingCart.addItem(ProductTestDataBuilder.someProduct().build(), new Quantity(2));
        shoppingCart.addItem(ProductTestDataBuilder.someProductAlt().build(), new Quantity(10));

        Billing billing = OrderTestDataBuilder.someBilling();
        Shipping shipping = OrderTestDataBuilder.someShipping();
        Quantity expectedItemsQuantity = shoppingCart.totalItems();
        Money expectedTotalAmount = shoppingCart.totalAmount().add(shipping.cost());

        Order order = checkoutService.checkout(customer, shoppingCart, billing, shipping, PaymentMethod.CREDIT_CARD);

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
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

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
                () -> checkoutService.checkout(customer, shoppingCart, billing, shipping, PaymentMethod.GATEWAY_BALANCE)
        );
        Assertions.assertThat(shoppingCart.isEmpty()).isFalse();
        Assertions.assertThat(shoppingCart.totalAmount()).isEqualTo(expectedTotalAmount);
        Assertions.assertThat(shoppingCart.totalItems()).isEqualTo(expectedItemsQuantity);
    }

    @Test
    void shouldGenerateExceptionWhenCheckoutEmptyShoppingCart() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someEmptyShoppingCart();
        Billing billing = OrderTestDataBuilder.someBilling();
        Shipping shipping = OrderTestDataBuilder.someShipping();

        Assertions.assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class).isThrownBy(
                () -> checkoutService.checkout(customer, shoppingCart, billing, shipping, PaymentMethod.GATEWAY_BALANCE)
        );
        Assertions.assertThat(shoppingCart.isEmpty()).isTrue();
    }

    @Test
    void givenValidShoppingCartAndCustomerWithFreeShipping_whenCheckout_shouldReturnPlacedOrderWithFreeShipping() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().loyaltyPoints(new LoyaltyPoints(3000)).build();

        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someEmptyShoppingCart();
        shoppingCart.addItem(ProductTestDataBuilder.someProduct().build(), new Quantity(2));
        shoppingCart.addItem(ProductTestDataBuilder.someProductAlt().build(), new Quantity(10));

        Billing billing = OrderTestDataBuilder.someBilling();
        Shipping shipping = OrderTestDataBuilder.someShipping();
        Quantity expectedItemsQuantity = shoppingCart.totalItems();
        Money expectedTotalAmount = shoppingCart.totalAmount();

        Order order = checkoutService.checkout(customer, shoppingCart, billing, shipping, PaymentMethod.CREDIT_CARD);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(order.customerId()).isNotNull(),
                o -> Assertions.assertThat(order.billing()).isNotNull(),
                o -> Assertions.assertThat(order.shipping()).isNotNull(),
                o -> Assertions.assertThat(order.shipping()).isEqualTo(shipping.toBuilder().cost(Money.ZERO).build()),
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
}