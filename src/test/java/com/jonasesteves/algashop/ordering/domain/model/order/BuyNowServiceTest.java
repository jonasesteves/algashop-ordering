package com.jonasesteves.algashop.ordering.domain.model.order;

import com.jonasesteves.algashop.ordering.domain.model.customer.Customer;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductOutOfStockException;
import com.jonasesteves.algashop.ordering.domain.model.commons.Money;
import com.jonasesteves.algashop.ordering.domain.model.product.Product;
import com.jonasesteves.algashop.ordering.domain.model.commons.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;

@ExtendWith(MockitoExtension.class)
class BuyNowServiceTest {

    @InjectMocks
    private BuyNowService buyNowService;

    @Mock
    private Orders orders;

    /*
    * Não é possível fazer com @Mock porque CustomerHasFreeShippingSpecification
    * possui valores primitivos que não são beans.
    * @Mock
    * private CustomerHasFreeShippingSpecification customerHasFreeShippingSpecification;
    */

    @BeforeEach
    void setup() {
        var specification = new CustomerHasFreeShippingSpecification(
                orders,
                new LoyaltyPoints(100),
                2L,
                new LoyaltyPoints(2000)
        );
        buyNowService = new BuyNowService(specification);
    }

    @Test
    void shouldGenerateValidOrder() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Quantity quantity = new Quantity(10);
        Product product = ProductTestDataBuilder.someProduct().build();
        Billing billing = OrderTestDataBuilder.someBilling();
        Shipping shipping = OrderTestDataBuilder.someShipping();

        Order order = buyNowService.buyNow(product, customer, billing, shipping, quantity, PaymentMethod.CREDIT_CARD);
        ProductId orderProductId = order.items().iterator().next().productId();
        Money totalAmountExpected = product.price().multiply(quantity).add(shipping.cost());

        Assertions.assertWith(order,
                o -> Assertions.assertThat(order.customerId()).isEqualTo(customer.id()),
                o -> Assertions.assertThat(order.billing()).isEqualTo(billing),
                o -> Assertions.assertThat(order.shipping()).isEqualTo(shipping),
                o -> Assertions.assertThat(order.items()).hasSize(1),
                o -> Assertions.assertThat(order.isPlaced()).isTrue(),
                o -> Assertions.assertThat(order.totalItems()).isEqualTo(quantity),
                o -> Assertions.assertThat(order.totalAmount()).isEqualTo(totalAmountExpected)
        );
        Assertions.assertThat(orderProductId).isEqualTo(product.id());
    }

    @Test
    void givenUnavailableProduct_whenBuyNow_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Quantity quantity = new Quantity(10);
        Product product = ProductTestDataBuilder.someUnavailableProduct().build();
        Billing billing = OrderTestDataBuilder.someBilling();
        Shipping shipping = OrderTestDataBuilder.someShipping();

        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class).isThrownBy(
                () -> buyNowService.buyNow(product, customer, billing, shipping, quantity, PaymentMethod.CREDIT_CARD)
        );
    }

    @Test
    void givenInvalidQuantity_whenTryToBuyNow_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Product product = ProductTestDataBuilder.someProduct().build();
        Billing billing = OrderTestDataBuilder.someBilling();
        Shipping shipping = OrderTestDataBuilder.someShipping();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> buyNowService.buyNow(product, customer, billing, shipping, Quantity.ZERO, PaymentMethod.CREDIT_CARD)
        );
    }

    @Test
    void givenValidProductAndDetails_whenBuyNow_shouldReturnPlacedOrder() {
        Product product = ProductTestDataBuilder.someProduct().build();
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Billing billingInfo = OrderTestDataBuilder.someBilling();
        Shipping shippingInfo = OrderTestDataBuilder.someShipping();
        Quantity quantity = new Quantity(3);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Order order = buyNowService.buyNow(product, customer, billingInfo, shippingInfo, quantity, paymentMethod);

        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(order.id()).isNotNull();
        Assertions.assertThat(order.customerId()).isEqualTo(customer.id());
        Assertions.assertThat(order.billing()).isEqualTo(billingInfo);
        Assertions.assertThat(order.shipping()).isEqualTo(shippingInfo);
        Assertions.assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        Assertions.assertThat(order.isPlaced()).isTrue();

        Assertions.assertThat(order.items()).hasSize(1);
        Assertions.assertThat(order.items().iterator().next().productId()).isEqualTo(product.id());
        Assertions.assertThat(order.items().iterator().next().quantity()).isEqualTo(quantity);
        Assertions.assertThat(order.items().iterator().next().price()).isEqualTo(product.price());

        Money expectedTotalAmount = product.price().multiply(quantity).add(shippingInfo.cost());
        Assertions.assertThat(order.totalAmount()).isEqualTo(expectedTotalAmount);
        Assertions.assertThat(order.totalItems()).isEqualTo(quantity);
    }

    @Test
    void givenCustomerWithFreeShipping_whenBuyNow_shouldReturnPlacedOrderWithFreeShipping() {
        Mockito.when(orders.salesQuantityByCustomerInYear(
                Mockito.any(CustomerId.class),
                Mockito.any(Year.class)
        )).thenReturn(2L);

        Product product = ProductTestDataBuilder.someProduct().build();
        Customer customer = CustomerTestDataBuilder.existingCustomer().loyaltyPoints(new LoyaltyPoints(100)).build();
        Billing billingInfo = OrderTestDataBuilder.someBilling();
        Shipping shippingInfo = OrderTestDataBuilder.someShipping();
        Quantity quantity = new Quantity(3);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Order order = buyNowService.buyNow(product, customer, billingInfo, shippingInfo, quantity, paymentMethod);

        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(order.id()).isNotNull();
        Assertions.assertThat(order.customerId()).isEqualTo(customer.id());
        Assertions.assertThat(order.billing()).isEqualTo(billingInfo);
        Assertions.assertThat(order.shipping()).isEqualTo(shippingInfo.toBuilder().cost(Money.ZERO).build());
        Assertions.assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        Assertions.assertThat(order.isPlaced()).isTrue();

        Assertions.assertThat(order.items()).hasSize(1);
        Assertions.assertThat(order.items().iterator().next().productId()).isEqualTo(product.id());
        Assertions.assertThat(order.items().iterator().next().quantity()).isEqualTo(quantity);
        Assertions.assertThat(order.items().iterator().next().price()).isEqualTo(product.price());

        Money expectedTotalAmount = product.price().multiply(quantity);
        Assertions.assertThat(order.totalAmount()).isEqualTo(expectedTotalAmount);
        Assertions.assertThat(order.totalItems()).isEqualTo(quantity);
    }
}