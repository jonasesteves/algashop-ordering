package com.jonasesteves.algashop.ordering.domain.model.repository;

import com.jonasesteves.algashop.ordering.domain.model.entity.Order;
import com.jonasesteves.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.provider.OrdersPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@DataJpaTest
@Import(OrdersPersistenceProvider.class)
class OrdersIT {

    private Orders orders;

    @Autowired
    public OrdersIT(Orders orders) {
        this.orders = orders;
    }

    @Test
    void shouldPersistAndFind() {
        Order order = OrderTestDataBuilder.someOrder().build();
        OrderId orderId = order.id();

        orders.add(order);
        Optional<Order> possibleOrder = orders.ofId(orderId);

        Assertions.assertThat(possibleOrder).isPresent();
        Order savedOrder = possibleOrder.get();
        Assertions.assertThat(savedOrder).satisfies(
                o -> Assertions.assertThat(o.id()).isEqualTo(orderId),
                o -> Assertions.assertThat(o.customerId()).isEqualTo(order.customerId()),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(order.totalAmount()),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(order.totalItems()),
                o -> Assertions.assertThat(o.placedAt()).isEqualTo(order.placedAt()),
                o -> Assertions.assertThat(o.paidAt()).isEqualTo(order.paidAt()),
                o -> Assertions.assertThat(o.canceledAt()).isEqualTo(order.canceledAt()),
                o -> Assertions.assertThat(o.readyAt()).isEqualTo(order.readyAt()),
                o -> Assertions.assertThat(o.status()).isEqualTo(order.status()),
                o -> Assertions.assertThat(o.paymentMethod()).isEqualTo(order.paymentMethod())
        );
    }
}