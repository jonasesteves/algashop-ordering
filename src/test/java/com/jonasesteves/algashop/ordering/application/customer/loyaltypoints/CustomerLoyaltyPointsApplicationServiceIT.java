package com.jonasesteves.algashop.ordering.application.customer.loyaltypoints;

import com.jonasesteves.algashop.ordering.domain.model.commons.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.customer.CantAddLoyaltyPointsOrderIsNotReadyException;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customer;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerArchivedException;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customers;
import com.jonasesteves.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.jonasesteves.algashop.ordering.domain.model.order.Order;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderId;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderNotBelongsToCustomerException;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderStatus;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.order.Orders;
import com.jonasesteves.algashop.ordering.domain.model.product.Product;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@Transactional
class CustomerLoyaltyPointsApplicationServiceIT {

    @Autowired
    private CustomerLoyaltyPointsApplicationService loyaltyPointsService;

    @Autowired
    private Customers customers;

    @Autowired
    private Orders orders;

    @Test
    void shouldAddLoyaltyPoints() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.someOrder()
                .withItems(true)
                .orderStatus(OrderStatus.DRAFT)
                .customerId(customer.id())
                .build();
        order.place();
        order.markAsPaid();
        order.markAsReady();

        orders.add(order);

        loyaltyPointsService.addLoyaltyPoints(customer.id().value(), order.id().toString());

        Customer updatedCustomer = customers.ofId(customer.id()).orElseThrow();

        Assertions.assertThat(updatedCustomer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(60));
    }

    @Test
    void givenCorrectOrder_whenAddLoyaltyPointsToInexistentCustomer_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.someOrder()
                .withItems(true)
                .orderStatus(OrderStatus.DRAFT)
                .customerId(customer.id())
                .build();
        order.place();
        order.markAsPaid();
        order.markAsReady();

        orders.add(order);

        UUID inexistentCustomerId = new CustomerId().value();
        String orderId = order.id().toString();

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class).isThrownBy(
                () -> loyaltyPointsService.addLoyaltyPoints(inexistentCustomerId, orderId)
        );
    }

    @Test
    void givenCorrectCustomer_whenAddLoyaltyPointsToInexistentOrder_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);

        UUID customerId = customer.id().value();
        String inexistentOrderId = new OrderId().toString();

        Assertions.assertThatExceptionOfType(OrderNotFoundException.class).isThrownBy(
                () -> loyaltyPointsService.addLoyaltyPoints(customerId, inexistentOrderId)
        );
    }

    @Test
    void givenCorrectOrder_whenAddLoyaltyPointsToArchivedCustomer_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customer.archive();
        customers.add(customer);

        Order order = OrderTestDataBuilder.someOrder()
                .withItems(true)
                .orderStatus(OrderStatus.DRAFT)
                .customerId(customer.id())
                .build();
        order.place();
        order.markAsPaid();
        order.markAsReady();

        orders.add(order);

        UUID customerId = customer.id().value();
        String orderId = order.id().toString();

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(
                () -> loyaltyPointsService.addLoyaltyPoints(customerId, orderId)
        );
    }

    @Test
    void givenCorrectOrder_whenAddLoyaltyPointsToDifferentCustomer_shouldGenerateException() {
        Customer customer1 = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer1);

        Customer customer2 = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer2);

        Order order = OrderTestDataBuilder.someOrder()
                .withItems(true)
                .orderStatus(OrderStatus.DRAFT)
                .customerId(customer1.id())
                .build();
        order.place();
        order.markAsPaid();
        order.markAsReady();

        orders.add(order);

        UUID customer2Id = customer2.id().value();
        String orderId = order.id().toString();

        Assertions.assertThatExceptionOfType(OrderNotBelongsToCustomerException.class).isThrownBy(
                () -> loyaltyPointsService.addLoyaltyPoints(customer2Id, orderId)
        );
    }

    @Test
    void givenNotReadyOrder_whenAddLoyaltyPoints_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.someOrder()
                .withItems(true)
                .orderStatus(OrderStatus.DRAFT)
                .customerId(customer.id())
                .build();
        order.place();

        orders.add(order);

        UUID customerId = customer.id().value();
        String orderId = order.id().toString();

        Assertions.assertThatExceptionOfType(CantAddLoyaltyPointsOrderIsNotReadyException.class).isThrownBy(
                () -> loyaltyPointsService.addLoyaltyPoints(customerId, orderId)
        );
    }

    @Test
    void givenOrderWithAmountLessThan1000_whenAddLoyaltyPointsToCustomer_shouldNotAdd() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);

        Product product = ProductTestDataBuilder.someProductAlt().build();
        Order order = OrderTestDataBuilder.someOrder()
                .withItems(false)
                .orderStatus(OrderStatus.DRAFT)
                .customerId(customer.id())
                .build();

        order.addItem(product, new Quantity(2));
        order.place();
        order.markAsPaid();
        order.markAsReady();

        orders.add(order);

        UUID customerId = customer.id().value();
        String orderId = order.id().toString();

        loyaltyPointsService.addLoyaltyPoints(customerId, orderId);
        Customer customer1 = customers.ofId(customer.id()).orElseThrow();

        Assertions.assertThat(customer1.loyaltyPoints().value()).isZero();
    }
}