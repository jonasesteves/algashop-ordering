package com.jonasesteves.algashop.ordering.domain.factory;

import com.jonasesteves.algashop.ordering.domain.entity.Order;
import com.jonasesteves.algashop.ordering.domain.entity.PaymentMethod;
import com.jonasesteves.algashop.ordering.domain.valueobject.Billing;
import com.jonasesteves.algashop.ordering.domain.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.valueobject.Shipping;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.CustomerId;

import java.util.Objects;

public class OrderFactory {

    private OrderFactory() {
    }

    public static Order filled(
            CustomerId customerId,
            Shipping shipping,
            Billing billing,
            PaymentMethod paymentMethod,
            Product product,
            Quantity quantity
    ) {
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(shipping);
        Objects.requireNonNull(billing);
        Objects.requireNonNull(paymentMethod);
        Objects.requireNonNull(product);
        Objects.requireNonNull(quantity);

        Order order = Order.draft(customerId);

        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);
        order.addItem(product, quantity);

        return order;
    }
}
