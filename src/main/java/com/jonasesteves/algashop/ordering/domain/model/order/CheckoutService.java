package com.jonasesteves.algashop.ordering.domain.model.order;

import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.jonasesteves.algashop.ordering.domain.model.DomainService;
import com.jonasesteves.algashop.ordering.domain.model.product.Product;

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
