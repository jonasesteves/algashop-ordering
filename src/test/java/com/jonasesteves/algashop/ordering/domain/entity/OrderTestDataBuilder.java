package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.valueobject.*;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.CustomerId;

import java.time.LocalDate;

public class OrderTestDataBuilder {

    private CustomerId customerId = new CustomerId();
    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
    private Shipping shipping = someShipping();
    private Billing billing = someBilling();
    private boolean withItems = true;
    private OrderStatus orderStatus = OrderStatus.DRAFT;

    private OrderTestDataBuilder() {
    }

    public static OrderTestDataBuilder someOrder() {
        return new OrderTestDataBuilder();
    }

    public Order build() {
        Order order = Order.draft(this.customerId);
        order.changeShipping(this.shipping);
        order.changeBilling(this.billing);
        order.changePaymentMethod(this.paymentMethod);

        if (this.withItems) {
            order.addItem(ProductTestDataBuilder.someProduct().build(), new Quantity(4));
            order.addItem(ProductTestDataBuilder.chocolate().build(), new Quantity(3));
        }

        switch (this.orderStatus) {
            case DRAFT -> {
            }
            case PLACED -> {
                order.place();
            }
            case PAID -> {
                order.place();
                order.markAsPaid();
            }
            case READY -> {
                order.place();
                order.markAsPaid();
                order.markAsReady();
            }
            case CANCELED -> {
                order.cancel();
            }
        }

        return order;
    }

    public static Billing someBilling() {
        return Billing.builder()
                .address(someAddress())
                .fullName(new FullName("Antony", "Edward Stark"))
                .phone(new Phone("555-111-2222"))
                .email(new Email("some@validemail.com"))
                .document(new Document("0102-03-0405"))
                .build();
    }

    public static Shipping someShipping() {
        Recipient recipient = Recipient.builder()
                .fullName(new FullName("Antony", "Stark"))
                .document(new Document("323-33-2323"))
                .phone(new Phone("555-555-5555"))
                .build();

        return Shipping.builder()
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().plusWeeks(1))
                .address(someAddress())
                .recipient(recipient)
                .build();
    }

    public static Shipping someAlternativeShipping() {
        Recipient recipient = Recipient.builder()
                .fullName(new FullName("Peter", "Parker"))
                .document(new Document("453-47-8690"))
                .phone(new Phone("555-58-5009"))
                .build();

        return Shipping.builder()
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().plusWeeks(1))
                .address(someAddress())
                .recipient(recipient)
                .build();
    }

    public static Address someAddress() {
        return Address.builder()
                .number("10880")
                .street("Malibu Point")
                .complement("Penthouse")
                .neighborhood("Malibu Beach")
                .city("Malibu")
                .state("California")
                .zipCode(new ZipCode("90265"))
                .build();
    }

    public static Address someAlternativeAddress() {
        return Address.builder()
                .number("2334")
                .street("Point Street")
                .complement("3rd Floor, 345")
                .neighborhood("Malibu Village")
                .city("Malibu")
                .state("California")
                .zipCode(new ZipCode("90233"))
                .build();
    }

    public OrderTestDataBuilder customerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderTestDataBuilder paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderTestDataBuilder shipping(Shipping shipping) {
        this.shipping = shipping;
        return this;
    }

    public OrderTestDataBuilder billing(Billing billing) {
        this.billing = billing;
        return this;
    }

    public OrderTestDataBuilder withItems(boolean withItems) {
        this.withItems = withItems;
        return this;
    }

    public OrderTestDataBuilder orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

}
