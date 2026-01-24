package com.jonasesteves.algashop.ordering.application.order.management;

import com.jonasesteves.algashop.ordering.domain.model.order.Order;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderId;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.order.Orders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class OrderManagementApplicationService {

    private final Orders orders;

    public OrderManagementApplicationService(Orders orders) {
        this.orders = orders;
    }

    @Transactional
    public void cancel(String rawOrderId) {
        Objects.requireNonNull(rawOrderId);
        Order order = findOrder(rawOrderId);
        order.cancel();
        orders.add(order);
    }

    @Transactional
    public void markAsPaid(String rawOrderId) {
        Objects.requireNonNull(rawOrderId);
        Order order = findOrder(rawOrderId);
        order.markAsPaid();
        orders.add(order);
    }

    @Transactional
    public void markAsReady(String rawOrderId) {
        Objects.requireNonNull(rawOrderId);
        Order order = findOrder(rawOrderId);
        order.markAsReady();
        orders.add(order);
    }

    private Order findOrder(String rawOrderId) {
        OrderId orderId = new OrderId(rawOrderId);
        return orders.ofId(orderId).orElseThrow(OrderNotFoundException::new);
    }
}
