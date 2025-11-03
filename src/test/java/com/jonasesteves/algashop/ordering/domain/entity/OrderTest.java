package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.exception.InvalidOrderDeliveryShippingDateException;
import com.jonasesteves.algashop.ordering.domain.exception.OrderCannotBeChangedException;
import com.jonasesteves.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.jonasesteves.algashop.ordering.domain.exception.ProductOutOfStockException;
import com.jonasesteves.algashop.ordering.domain.valueobject.*;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

class OrderTest {

    @Test
    public void shouldGenerateDraftOrder() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.customerId()).isNotNull(),
                o -> Assertions.assertThat(o.customerId()).isEqualTo(customerId),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(Money.ZERO),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(Quantity.ZERO),
                o -> Assertions.assertThat(o.isDraft()).isTrue(),
                o -> Assertions.assertThat(o.items()).isEmpty(),
                o -> Assertions.assertThat(o.placedAt()).isNull(),
                o -> Assertions.assertThat(o.paidAt()).isNull(),
                o -> Assertions.assertThat(o.canceledAt()).isNull(),
                o -> Assertions.assertThat(o.readyAt()).isNull(),
                o -> Assertions.assertThat(o.billing()).isNull(),
                o -> Assertions.assertThat(o.shipping()).isNull(),
                o -> Assertions.assertThat(o.paymentMethod()).isNull()
        );
    }

    @Test
    void shouldAddItem() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.someProduct().build();
        ProductId productId = product.id();

        order.addItem(product, new Quantity(2));

        OrderItem item = order.items().iterator().next();

        Assertions.assertThat(order.items().size()).isEqualTo(1);
        Assertions.assertWith(item,
            i -> Assertions.assertThat(i.id()).isNotNull(),
                i -> Assertions.assertThat(i.productName().value()).isEqualTo("Adamantium"),
                i -> Assertions.assertThat(i.productId().value()).isEqualTo(productId.value()),
                i -> Assertions.assertThat(i.price().value()).hasToString("3000.00"),
                i -> Assertions.assertThat(i.quantity().value()).isEqualTo(2)
        );
    }

    @Test
    void givenAnItemsSet_whenTryToChange_thenGenerateException() {
        Order order = Order.draft(new CustomerId());

        order.addItem(ProductTestDataBuilder.chocolate().build(), new Quantity(2));

        Set<OrderItem> items = order.items();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy((items::clear));
    }

    @Test
    void shouldRecalculateTotals() {
        Order order = Order.draft(new CustomerId());

        order.addItem(ProductTestDataBuilder.chocolate().build(), new Quantity(2));
        order.addItem(ProductTestDataBuilder.someProduct().build(), new Quantity(2));

        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("6001.98"));
        Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(4));
    }

    @Test
    void givenDraftOrder_whenPlace_shouldChangeToPlaced() {
        Order order = OrderTestDataBuilder.someOrder().build();
        order.place();

        Assertions.assertThat(order.isPlaced()).isTrue();
    }

    @Test
    void givenPlacedOrder_whenMarkAsPaid_shouldChangeToPaid() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.PLACED).build();
        order.markAsPaid();

        Assertions.assertThat(order.isPaid()).isTrue();
        Assertions.assertThat(order.paidAt()).isNotNull();
    }

    @Test
    void givenPlacedOrder_whenTryToPlace_shouldGenerateException() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.PLACED).build();
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class).isThrownBy(order::place);
    }

    @Test
    void givenDraftOrder_whenChangePayment_ShouldAllowChange() {
        Order order = Order.draft(new CustomerId());
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

        Assertions.assertWith(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    void givenDraftOrder_whenChangeBilling_shouldAllowChange() {
        Billing billing = OrderTestDataBuilder.someBilling();
        Order order = Order.draft(new CustomerId());

        order.changeBilling(billing);

        Assertions.assertThat(order.billing()).isEqualTo(billing);
    }

    @Test
    void givenDraftOrder_whenChangeShipping_thenAllowChange() {
        Shipping shipping = OrderTestDataBuilder.someShipping();
        Order order = Order.draft(new CustomerId());

        order.changeShipping(shipping);

        Assertions.assertWith(order, o -> Assertions.assertThat(o.shipping()).isEqualTo(shipping));
    }

    @Test
    void givenDraftOrderWithDeliveryDateInThePast_whenChangeShipping_thenGenerateException() {
        LocalDate expectedDeliveryDate = LocalDate.now().minusDays(1);
        Shipping shipping = OrderTestDataBuilder.someShipping().toBuilder()
                .expectedDate(expectedDeliveryDate)
                .build();

        Order order = Order.draft(new CustomerId());

        Assertions.assertThatExceptionOfType(InvalidOrderDeliveryShippingDateException.class).isThrownBy(
                () -> order.changeShipping(shipping)
        );
    }

    @Test
    void givenDraftOrder_whenChangeItem_shouldRecalculate() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                ProductTestDataBuilder.someProduct().build(),
                new Quantity(1)
        );
        OrderItem orderItem = order.items().iterator().next();
        order.changeItemQuantity(orderItem.id(), new Quantity(5));

        Assertions.assertWith(order,
                i -> Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("15000.00")),
                i -> Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(5))
        );
    }

    @Test
    void givenOutOfStockProduct_whenTryToAdd_shouldGenerateException() {
        Order order = Order.draft(new CustomerId());

        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> order.addItem(ProductTestDataBuilder.someUnavailableProduct().build(), new Quantity(1)));
    }

    @Test
    void givenDraftOrder_whenChange_ShouldChangeSuccessfuly() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.someProduct().build();
        Shipping shipping = OrderTestDataBuilder.someShipping();
        Billing billing = OrderTestDataBuilder.someBilling();
        PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
        Quantity quantity = new Quantity(2);

        order.addItem(product, quantity);
        order.changeShipping(shipping);
        order.changeBilling(billing);
        order.changePaymentMethod(paymentMethod);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.shipping()).isEqualTo(shipping),
                o -> Assertions.assertThat(o.billing()).isEqualTo(billing),
                o -> Assertions.assertThat(o.paymentMethod()).isEqualTo(paymentMethod),
                o -> Assertions.assertThat(o.items()).isNotEmpty()
        );
    }

    @Test
    void givenNotDraftOrder_whenTryToChange_shouldGenerateException() {
        Order order = OrderTestDataBuilder.someOrder().build();
        order.place();

        Product product = ProductTestDataBuilder.someProduct().build();
        Quantity quantity = new Quantity(2);
        Shipping shipping = OrderTestDataBuilder.someShipping();
        Billing billing = OrderTestDataBuilder.someBilling();

        Assertions.assertThatExceptionOfType(OrderCannotBeChangedException.class).isThrownBy(() -> order.changeBilling(billing));
        Assertions.assertThatExceptionOfType(OrderCannotBeChangedException.class).isThrownBy(() -> order.changeShipping(shipping));
        Assertions.assertThatExceptionOfType(OrderCannotBeChangedException.class).isThrownBy(() -> order.changePaymentMethod(PaymentMethod.CREDIT_CARD));
        Assertions.assertThatExceptionOfType(OrderCannotBeChangedException.class).isThrownBy(() -> order.addItem(product, quantity));
    }
}