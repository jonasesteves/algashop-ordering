package com.jonasesteves.algashop.ordering.application.checkout;

import com.jonasesteves.algashop.ordering.domain.model.order.PaymentMethod;
import com.jonasesteves.algashop.ordering.domain.model.order.shipping.OriginAddressService;
import com.jonasesteves.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CheckoutApplicationService {

    private final ShoppingCarts shoppingCarts;

    public CheckoutApplicationService(ShoppingCarts shoppingCarts) {
        this.shoppingCarts = shoppingCarts;
    }

    public void checkout(CheckoutInput checkoutInput) {
        Objects.requireNonNull(checkoutInput);
        PaymentMethod paymentMethod = PaymentMethod.valueOf(checkoutInput.getPaymentMethod());

        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(checkoutInput.getShoppingCartId()))
                .orElseThrow(ShoppingCartNotFoundException::new);

//        ShippingCostService;
//        OriginAddressService
    }
}
