package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderCancelTest {

    @Test
    void givenRegularOrder_whenTryToCancel_shouldCancelSuccessfully() {
        Order order = Order.draft(new CustomerId());
        order.cancel();

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                o -> Assertions.assertThat(o.isCanceled()).isTrue(),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull()
        );
    }

    @Test
    void givenFilledOrder_whenTryToCancel_shouldCancelSuccessfully() {
        Order order = OrderTestDataBuilder.someOrder().build();
        order.cancel();

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                o -> Assertions.assertThat(o.isCanceled()).isTrue(),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull()
        );
    }

    @Test
    void givenCanceledOrder_whenTryToCancel_shouldGenerateException() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.CANCELED).build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class).isThrownBy(order::cancel);
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                o -> Assertions.assertThat(o.isCanceled()).isTrue(),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull()
        );
    }
}
