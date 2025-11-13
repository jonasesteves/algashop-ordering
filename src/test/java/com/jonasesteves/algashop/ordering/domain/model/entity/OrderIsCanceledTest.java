package com.jonasesteves.algashop.ordering.domain.model.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderIsCanceledTest {

    @Test
    void shouldReturnTrueOnlyWhenStatusIsCanceled() {
        Order order = OrderTestDataBuilder.someOrder().build();
        Assertions.assertThat(order.isCanceled()).isFalse();

        order.place();
        Assertions.assertThat(order.isCanceled()).isFalse();

        order.markAsPaid();
        Assertions.assertThat(order.isCanceled()).isFalse();

        order.markAsReady();
        Assertions.assertThat(order.isCanceled()).isFalse();

        order.cancel();
        Assertions.assertThat(order.isCanceled()).isTrue();
    }
}
