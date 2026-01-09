package com.jonasesteves.algashop.ordering.domain.model.order;

import com.jonasesteves.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.product.Product;
import com.jonasesteves.algashop.ordering.domain.model.commons.Quantity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderChangingTest {

    @Test
    void givenDraftOrder_whenChange_ShouldChangeSuccessfuly() {
        Order order = OrderTestDataBuilder.someOrder().build();

        Product product = ProductTestDataBuilder.someProduct().build();
        Quantity quantity = new Quantity(2);
        Shipping shipping = OrderTestDataBuilder.someShipping();
        Billing billing = OrderTestDataBuilder.someBilling();
        PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

        OrderItem item = order.items().iterator().next();

        Assertions.assertThatCode(() -> order.addItem(product, quantity)).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> order.changeItemQuantity(item.id(), quantity)).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> order.changeShipping(shipping)).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> order.changeBilling(billing)).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> order.changePaymentMethod(paymentMethod)).doesNotThrowAnyException();
    }

    @Test
    void givenNotDraftOrder_whenTryToChange_shouldGenerateException() {
        Order order = OrderTestDataBuilder.someOrder().build();
        order.place();

        Product product = ProductTestDataBuilder.someProduct().build();
        Quantity quantity = new Quantity(2);
        Shipping shipping = OrderTestDataBuilder.someShipping();
        Billing billing = OrderTestDataBuilder.someBilling();

        Assertions.assertThatExceptionOfType(OrderCannotBeChangedException.class).isThrownBy(() -> order.changeBilling(billing));
        Assertions.assertThatExceptionOfType(OrderCannotBeChangedException.class).isThrownBy(() -> order.changeShipping(shipping));
        Assertions.assertThatExceptionOfType(OrderCannotBeChangedException.class).isThrownBy(() -> order.changePaymentMethod(PaymentMethod.CREDIT_CARD));
        Assertions.assertThatExceptionOfType(OrderCannotBeChangedException.class).isThrownBy(() -> order.addItem(product, quantity));
    }

}
