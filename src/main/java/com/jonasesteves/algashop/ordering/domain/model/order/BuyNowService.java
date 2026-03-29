package com.jonasesteves.algashop.ordering.domain.model.order;

import com.jonasesteves.algashop.ordering.domain.model.CustomerHaveFreeShippingSpecification;
import com.jonasesteves.algashop.ordering.domain.model.DomainService;
import com.jonasesteves.algashop.ordering.domain.model.commons.Money;
import com.jonasesteves.algashop.ordering.domain.model.commons.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customer;
import com.jonasesteves.algashop.ordering.domain.model.product.Product;

@DomainService
public class BuyNowService {

    private final CustomerHaveFreeShippingSpecification customerHaveFreeShippingSpecification;

    public BuyNowService(CustomerHaveFreeShippingSpecification customerHaveFreeShippingSpecification) {
        this.customerHaveFreeShippingSpecification = customerHaveFreeShippingSpecification;
    }

    public Order buyNow(Product product, Customer customer, Billing billing, Shipping shipping, Quantity quantity, PaymentMethod paymentMethod) {
        product.checkOutOfStock();

        Order order = Order.draft(customer.id());
        order.changeBilling(billing);
        order.changePaymentMethod(paymentMethod);
        order.addItem(product, quantity);

        if (haveFreeShipping(customer)) {
            Shipping freeShipping = shipping.toBuilder().cost(Money.ZERO).build();
            order.changeShipping(freeShipping);
        } else {
            order.changeShipping(shipping);
        }

        order.place();

        return order;
    }

    private boolean haveFreeShipping(Customer customer) {
        return customerHaveFreeShippingSpecification.isSatisfiedBy(customer);
    }
}
