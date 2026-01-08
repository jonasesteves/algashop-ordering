package com.jonasesteves.algashop.ordering.domain.model.service;

import com.jonasesteves.algashop.ordering.domain.model.entity.Order;
import com.jonasesteves.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.entity.PaymentMethod;
import com.jonasesteves.algashop.ordering.domain.model.entity.ProductTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.exception.ProductOutOfStockException;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Billing;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Shipping;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BuyNowServiceTest {

    private final BuyNowService buyNowService = new BuyNowService();

    @Test
    void shouldGenerateValidOrder() {
        CustomerId customerId = new CustomerId();
        Quantity quantity = new Quantity(10);
        Product product = ProductTestDataBuilder.someProduct().build();
        Billing billing = OrderTestDataBuilder.someBilling();
        Shipping shipping = OrderTestDataBuilder.someShipping();

        Order order = buyNowService.buyNow(product, customerId, billing, shipping, quantity, PaymentMethod.CREDIT_CARD);
        ProductId orderProductId = order.items().iterator().next().productId();
        Money totalAmountExpected = product.price().multiply(quantity).add(shipping.cost());

        Assertions.assertWith(order,
                o -> Assertions.assertThat(order.customerId()).isEqualTo(customerId),
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
        CustomerId customerId = new CustomerId();
        Quantity quantity = new Quantity(10);
        Product product = ProductTestDataBuilder.someUnavailableProduct().build();
        Billing billing = OrderTestDataBuilder.someBilling();
        Shipping shipping = OrderTestDataBuilder.someShipping();

        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class).isThrownBy(
                () -> buyNowService.buyNow(product, customerId, billing, shipping, quantity, PaymentMethod.CREDIT_CARD)
        );
    }

    @Test
    void givenInvalidQuantity_whenTryToBuyNow_shouldGenerateException() {
        CustomerId customerId = new CustomerId();
        Product product = ProductTestDataBuilder.someProduct().build();
        Billing billing = OrderTestDataBuilder.someBilling();
        Shipping shipping = OrderTestDataBuilder.someShipping();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> buyNowService.buyNow(product, customerId, billing, shipping, Quantity.ZERO, PaymentMethod.CREDIT_CARD)
        );
    }
}