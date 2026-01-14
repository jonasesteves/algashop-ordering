package com.jonasesteves.algashop.ordering.application.customer.loyaltypoints;

import com.jonasesteves.algashop.ordering.domain.model.customer.Customer;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerLoyaltyPointsService;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customers;
import com.jonasesteves.algashop.ordering.domain.model.order.Order;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderId;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.order.Orders;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class CustomerLoyaltyPointsApplicationService {

    private final Orders orders;
    private final Customers customers;
    private final CustomerLoyaltyPointsService customerLoyaltyPointsService;

    public CustomerLoyaltyPointsApplicationService(Orders orders, Customers customers, CustomerLoyaltyPointsService customerLoyaltyPointsService) {
        this.orders = orders;
        this.customers = customers;
        this.customerLoyaltyPointsService = customerLoyaltyPointsService;
    }

    public void addLoyaltyPoints(UUID rawCustomerId, String rawOrderId) {
        Objects.requireNonNull(rawCustomerId);
        Objects.requireNonNull(rawOrderId);

        Order order = orders.ofId(new OrderId(rawOrderId)).orElseThrow(OrderNotFoundException::new);
        Customer customer = customers.ofId(new CustomerId(rawCustomerId)).orElseThrow(CustomerNotFoundException::new);

        customerLoyaltyPointsService.addPoints(customer, order);

        customers.add(customer);
    }
}
