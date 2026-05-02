package com.jonasesteves.algashop.ordering.application.checkout;

import com.jonasesteves.algashop.ordering.domain.model.commons.ZipCode;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customer;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customers;
import com.jonasesteves.algashop.ordering.domain.model.order.*;
import com.jonasesteves.algashop.ordering.domain.model.order.shipping.OriginAddressService;
import com.jonasesteves.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CheckoutApplicationService {

    private final ShoppingCarts shoppingCarts;
    private final ShippingCostService shippingCostService;
    private final OriginAddressService originAddressService;
    private final Customers customers;
    private final CheckoutService checkoutService;
    private final ShippingInputDisassembler shippingInputDisassembler;
    private final BillingInputDisassembler billingInputDisassembler;
    private final Orders orders;

    public CheckoutApplicationService(ShoppingCarts shoppingCarts, ShippingCostService shippingCostService, OriginAddressService originAddressService, Customers customers, CheckoutService checkoutService, ShippingInputDisassembler shippingInputDisassembler, BillingInputDisassembler billingInputDisassembler, Orders orders) {
        this.shoppingCarts = shoppingCarts;
        this.shippingCostService = shippingCostService;
        this.originAddressService = originAddressService;
        this.customers = customers;
        this.checkoutService = checkoutService;
        this.shippingInputDisassembler = shippingInputDisassembler;
        this.billingInputDisassembler = billingInputDisassembler;
        this.orders = orders;
    }

    @Transactional
    public String checkout(CheckoutInput checkoutInput) {
        Objects.requireNonNull(checkoutInput);
        PaymentMethod paymentMethod = PaymentMethod.valueOf(checkoutInput.getPaymentMethod());

        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(checkoutInput.getShoppingCartId()))
                .orElseThrow(ShoppingCartNotFoundException::new);

        Customer customer = customers.ofId(shoppingCart.customerId()).orElseThrow(CustomerNotFoundException::new);

        ShippingCostService.CalculationResult shippingCalculationResult = calculateShippingCost(checkoutInput.getShipping());

        Shipping shipping = shippingInputDisassembler.toDomainModel(checkoutInput.getShipping(),  shippingCalculationResult);
        Billing billing = billingInputDisassembler.toDomainModel(checkoutInput.getBilling());

        Order order = checkoutService.checkout(customer, shoppingCart, billing, shipping, paymentMethod);
        orders.add(order);
        shoppingCarts.add(shoppingCart);

        return order.id().toString();

    }

    private ShippingCostService.CalculationResult calculateShippingCost(ShippingInput shipping) {
        ZipCode origin = originAddressService.originAddress().zipCode();
        ZipCode destination = new ZipCode(shipping.getAddress().getZipCode());

        return shippingCostService.calculate(new ShippingCostService.CalculationRequest(origin, destination));
    }
}
