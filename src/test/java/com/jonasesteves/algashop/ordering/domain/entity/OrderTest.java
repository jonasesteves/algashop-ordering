package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.exception.InvalidOrderDeliveryShippingDateException;
import com.jonasesteves.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.jonasesteves.algashop.ordering.domain.valueobject.*;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

class OrderTest {

    @Test
    public void shouldGenerate() {
        Order.draft(new CustomerId());
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
    void givenDraftOrder_whenChangeBillingInfo_shouldAllowChange() {
        Address address = Address.builder()
                .street("Any Street")
                .number("2222")
                .neighborhood("New Ville")
                .city("New York")
                .state("New York")
                .zipCode(new ZipCode("32154"))
                .build();

        BillingInfo billingInfo = BillingInfo.builder()
                .address(address)
                .document(new Document("222-00-0999"))
                .phone(new Phone("555-555-5555"))
                .fullName(new FullName("Stanley", "Raynold"))
                .build();

        Order order = Order.draft(new CustomerId());
        order.changeBilling(billingInfo);

        Assertions.assertThat(order.billing()).isEqualTo(billingInfo);
    }

    @Test
    void givenDraftOrder_whenChangeShippingInfo_thenAllowChange() {
        Address address = Address.builder()
                .street("Any Street")
                .number("2222")
                .neighborhood("New Ville")
                .city("New York")
                .state("New York")
                .zipCode(new ZipCode("32154"))
                .build();

        ShippingInfo shippingInfo = ShippingInfo.builder()
                .address(address)
                .fullName(new FullName("Bryan", "Raynold"))
                .document(new Document("323-33-2323"))
                .phone(new Phone("555-555-5555"))
                .build();

        Order order = Order.draft(new CustomerId());
        Money shippingCost = new Money("0");
        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(1);

        order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.shipping()).isEqualTo(shippingInfo),
                o -> Assertions.assertThat(o.shippingCost()).isEqualTo(shippingCost),
                o -> Assertions.assertThat(o.expectedDeliveryDate()).isEqualTo(expectedDeliveryDate)
        );
    }

    @Test
    void givenDraftOrderWithDeliveryDateInThePast_whenChangeShippingInfo_thenGenerateException() {
        Address address = Address.builder()
                .street("Any Street")
                .number("2222")
                .neighborhood("New Ville")
                .city("New York")
                .state("New York")
                .zipCode(new ZipCode("32154"))
                .build();

        ShippingInfo shippingInfo = ShippingInfo.builder()
                .address(address)
                .fullName(new FullName("Bryan", "Edward Stark"))
                .phone(new Phone("555-111-2222"))
                .document(new Document("0102-03-0405"))
                .build();

        Order order = Order.draft(new CustomerId());
        Money shippingCost = new Money("0");
        LocalDate expectedDeliveryDate = LocalDate.now().minusDays(1);


        Assertions.assertThatExceptionOfType(InvalidOrderDeliveryShippingDateException.class).isThrownBy(
                () -> order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate)
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

}