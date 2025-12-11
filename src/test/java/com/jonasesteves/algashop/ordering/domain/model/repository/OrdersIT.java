package com.jonasesteves.algashop.ordering.domain.model.repository;

import com.jonasesteves.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.entity.Order;
import com.jonasesteves.algashop.ordering.domain.model.entity.OrderStatus;
import com.jonasesteves.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.provider.CustomerPersistenceProvider;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.provider.OrdersPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

@DataJpaTest
@Import({
        OrdersPersistenceProvider.class,
        OrderPersistenceEntityAssembler.class,
        OrderPersistenceEntityDisassembler.class,
        CustomerPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class
})
class OrdersIT {

    private final Orders orders;
    private final Customers customers;

    @Autowired
    public OrdersIT(Orders orders, Customers customers) {
        this.orders = orders;
        this.customers = customers;
    }

    @BeforeEach
    void setup() {
        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
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

    @Test
    void shouldUpdateExistingOrder() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.PLACED).build();
        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();
        order.markAsPaid();
        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();

        Assertions.assertThat(order.isPaid()).isTrue();
    }

    @Test
    void shouldNotAllowStaleUpdates() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.PLACED).build();
        orders.add(order);

        Order orderT1 = orders.ofId(order.id()).orElseThrow();
        Order orderT2 = orders.ofId(order.id()).orElseThrow();

        orderT1.markAsPaid();
        orders.add(orderT1);

        orderT2.cancel();

        Assertions.assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class).isThrownBy(() -> orders.add(orderT2));

        Order savedOrder = orders.ofId(order.id()).orElseThrow();

        Assertions.assertThat(savedOrder.canceledAt()).isNull();
        Assertions.assertThat(savedOrder.paidAt()).isNotNull();
    }

    @Test
    void shouldCountExistingOrders() {
        Assertions.assertThat(orders.count()).isZero();

        Order order1 = OrderTestDataBuilder.someOrder().build();
        Order order2 = OrderTestDataBuilder.someOrder().build();
        orders.add(order1);
        orders.add(order2);

        Assertions.assertThat(orders.count()).isEqualTo(2L);
    }

    @Test
    void shouldReturnIfOrderExists() {
        Order order = OrderTestDataBuilder.someOrder().build();
        orders.add(order);

        Assertions.assertThat(orders.exists(order.id())).isTrue();
        Assertions.assertThat(orders.exists(new OrderId())).isFalse();
    }
}