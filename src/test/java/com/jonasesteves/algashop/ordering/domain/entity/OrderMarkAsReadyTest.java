package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderMarkAsReadyTest {

    @Test
    void givenOrderWithStatusPaid_whenMarkAsReady_shouldChangeSuccessfully() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.PAID).build();

        order.markAsReady();

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.READY),
                o -> Assertions.assertThat(o.readyAt()).isNotNull()
        );
    }

    @Test
    void givenDraftOrder_whenMarkAsReady_shouldGenerateException() {
        Order order = OrderTestDataBuilder.someOrder().build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::markAsReady);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.DRAFT),
                o -> Assertions.assertThat(o.readyAt()).isNull()
        );
    }

    @Test
    void givenPlacedOrder_whenMarkAsReady_shouldGenerateException() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.PLACED).build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::markAsReady);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.PLACED),
                o -> Assertions.assertThat(o.readyAt()).isNull()
        );
    }

    @Test
    void givenReadydOrder_whenMarkAsReady_shouldGenerateException() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.READY).build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::markAsReady);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.READY),
                o -> Assertions.assertThat(o.readyAt()).isNotNull()
        );
    }
}
