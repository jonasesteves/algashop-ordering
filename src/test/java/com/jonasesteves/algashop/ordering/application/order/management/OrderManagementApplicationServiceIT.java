package com.jonasesteves.algashop.ordering.application.order.management;

import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customers;
import com.jonasesteves.algashop.ordering.domain.model.order.Order;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderId;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderStatus;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderStatusCannotBeChangedException;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.order.Orders;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
class OrderManagementApplicationServiceIT {

    @Autowired
    private OrderManagementApplicationService service;

    @Autowired
    private Orders orders;

    @Autowired
    private Customers customers;

    @BeforeEach
    void setup() {
        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    void shouldCancelOrderSuccessfully() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.PLACED).build();
        orders.add(order);

        service.cancel(order.id().toString());

        Optional<Order> updatedOrder = orders.ofId(order.id());
        Assertions.assertThat(updatedOrder).isPresent();
        Assertions.assertThat(updatedOrder.get().status()).isEqualTo(OrderStatus.CANCELED);
        Assertions.assertThat(updatedOrder.get().canceledAt()).isNotNull();
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenCancellingNonExistingOrder() {
        String nonExistingOrderId = new OrderId().toString();

        Assertions.assertThatExceptionOfType(OrderNotFoundException.class)
                .isThrownBy(() -> service.cancel(nonExistingOrderId));
    }

    @Test
    void shouldThrowOrderStatusCannotBeChangedExceptionWhenCancellingAlreadyCanceledOrder() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.CANCELED).build();
        orders.add(order);

        String orderId = order.id().toString();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> service.cancel(orderId));
    }

    @Test
    void shouldMarkOrderAsPaidSuccessfully() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.PLACED).build();
        orders.add(order);

        service.markAsPaid(order.id().toString());

        Optional<Order> updatedOrder = orders.ofId(order.id());
        Assertions.assertThat(updatedOrder).isPresent();
        Assertions.assertThat(updatedOrder.get().status()).isEqualTo(OrderStatus.PAID);
        Assertions.assertThat(updatedOrder.get().paidAt()).isNotNull();
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenMarkingNonExistingOrderAsPaid() {
        String nonExistingOrderId = new OrderId().toString();

        Assertions.assertThatExceptionOfType(OrderNotFoundException.class)
                .isThrownBy(() -> service.markAsPaid(nonExistingOrderId));
    }

    @Test
    void shouldThrowOrderStatusCannotBeChangedExceptionWhenMarkingAlreadyPaidOrderAsPaid() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.PAID).build();
        orders.add(order);

        String orderId = order.id().toString();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> service.markAsPaid(orderId));
    }

    @Test
    void shouldThrowOrderStatusCannotBeChangedExceptionWhenMarkingCanceledOrderAsPaid() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.CANCELED).build();
        orders.add(order);

        String orderId = order.id().toString();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> service.markAsPaid(orderId));
    }

    @Test
    void shouldMarkOrderAsReadySuccessfully() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.PAID).build();
        orders.add(order);

        service.markAsReady(order.id().toString());

        Optional<Order> updatedOrder = orders.ofId(order.id());
        Assertions.assertThat(updatedOrder).isPresent();
        Assertions.assertThat(updatedOrder.get().status()).isEqualTo(OrderStatus.READY);
        Assertions.assertThat(updatedOrder.get().readyAt()).isNotNull();
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenMarkingNonExistingOrderAsReady() {
        String nonExistingOrderId = new OrderId().toString();

        Assertions.assertThatExceptionOfType(OrderNotFoundException.class)
                .isThrownBy(() -> service.markAsReady(nonExistingOrderId));
    }

    @Test
    void shouldThrowOrderStatusCannotBeChangedExceptionWhenMarkingAlreadyReadyOrderAsReady() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.READY).build();
        orders.add(order);

        String orderId = order.id().toString();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> service.markAsReady(orderId));
    }

    @Test
    void shouldThrowOrderStatusCannotBeChangedExceptionWhenMarkingPlacedOrderAsReady() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.PLACED).build();
        orders.add(order);

        String orderId = order.id().toString();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> service.markAsReady(orderId));
    }
}