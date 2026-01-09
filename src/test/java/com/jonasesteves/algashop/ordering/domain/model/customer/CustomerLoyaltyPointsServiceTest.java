package com.jonasesteves.algashop.ordering.domain.model.customer;

import com.jonasesteves.algashop.ordering.domain.model.order.Order;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderStatus;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.product.Product;
import com.jonasesteves.algashop.ordering.domain.model.commons.Quantity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomerLoyaltyPointsServiceTest {

    CustomerLoyaltyPointsService customerLoyaltyPointsService = new CustomerLoyaltyPointsService();

    @Test
    void givenValidCustomerAndOrder_whenAddingPoints_shouldAccumulate() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.READY).build();

        customerLoyaltyPointsService.addPoints(customer, order);

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(60));
    }

    @Test
    void givenValidCustomerWithLowTotalAmount_whenAddingPoints_shouldNotAccumulate() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Product product = ProductTestDataBuilder.someProductAlt().build();

        Order order = OrderTestDataBuilder.someOrder().withItems(false).orderStatus(OrderStatus.DRAFT).build();
        order.addItem(product, new Quantity(10));
        order.place();
        order.markAsPaid();
        order.markAsReady();

        customerLoyaltyPointsService.addPoints(customer, order);

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(0));
    }

}