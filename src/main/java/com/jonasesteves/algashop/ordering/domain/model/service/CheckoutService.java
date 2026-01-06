package com.jonasesteves.algashop.ordering.domain.model.service;

import com.jonasesteves.algashop.ordering.domain.model.entity.Order;
import com.jonasesteves.algashop.ordering.domain.model.entity.PaymentMethod;
import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.jonasesteves.algashop.ordering.domain.model.exception.ShoppingCartCantProceedToCheckoutException;
import com.jonasesteves.algashop.ordering.domain.model.utility.DomainService;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Billing;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Shipping;

@DomainService
public class CheckoutService {

    public Order checkout(ShoppingCart shoppingCart, Billing billing, Shipping shipping, PaymentMethod paymentMethod) {
        if (shoppingCart.containsUnavailableItems() || shoppingCart.isEmpty()) {
            throw new ShoppingCartCantProceedToCheckoutException();
        }

        Order order = Order.draft(shoppingCart.customerId());
        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);

        shoppingCart.items().forEach(i -> order.addItem(this.extractProduct(i), i.quantity()));

        order.place();
        shoppingCart.empty();

        return order;
    }

    private Product extractProduct(ShoppingCartItem i) {
        return Product.builder()
                .id(i.productId())
                .name(i.name())
                .price(i.price())
                .inStock(i.available())
                .build();
    }
}
