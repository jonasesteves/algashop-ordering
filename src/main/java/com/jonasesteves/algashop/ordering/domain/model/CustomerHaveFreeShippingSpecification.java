package com.jonasesteves.algashop.ordering.domain.model;

import com.jonasesteves.algashop.ordering.domain.model.customer.Customer;
import com.jonasesteves.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.jonasesteves.algashop.ordering.domain.model.order.Orders;

import java.time.Year;

public class CustomerHaveFreeShippingSpecification implements Specification<Customer> {

    private final int minPointsForFreeShippingRule1;
    private final int minPointsForFreeShippingRule2;
    private final long salesQuantityForFreeShippingRule1;

    private final Orders orders;

    public CustomerHaveFreeShippingSpecification(int minPointsForFreeShippingRule1, int minPointsForFreeShippingRule2, long salesQuantityForFreeShippingRule1, Orders orders) {
        this.minPointsForFreeShippingRule1 = minPointsForFreeShippingRule1;
        this.minPointsForFreeShippingRule2 = minPointsForFreeShippingRule2;
        this.salesQuantityForFreeShippingRule1 = salesQuantityForFreeShippingRule1;
        this.orders = orders;
    }

    @Override
    public boolean isSatisfiedBy(Customer customer) {
        return customer.loyaltyPoints().compareTo(new LoyaltyPoints(minPointsForFreeShippingRule1)) >= 0
                && orders.salesQuantityByCustomerInYear(customer.id(), Year.now()) >= salesQuantityForFreeShippingRule1
                || customer.loyaltyPoints().compareTo(new LoyaltyPoints(minPointsForFreeShippingRule2)) >= 0;
    }
}
