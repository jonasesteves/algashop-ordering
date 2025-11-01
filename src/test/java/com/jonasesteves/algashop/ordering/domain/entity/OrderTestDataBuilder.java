package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.valueobject.*;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ProductId;

import java.time.LocalDate;

public class OrderTestDataBuilder {

    private CustomerId customerId = new CustomerId();
    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
    private Money shippingCost = new Money("10.00");
    private LocalDate expectedDeliveryDate = LocalDate.now().plusWeeks(1);
    private ShippingInfo shippingInfo = someShippingInfo();
    private BillingInfo billingInfo = someBillingInfo();
    private boolean withItems = true;
    private OrderStatus orderStatus = OrderStatus.DRAFT;

    public OrderTestDataBuilder() {
    }

    public static OrderTestDataBuilder someOrder() {
        return new OrderTestDataBuilder();
    }

    public Order build() {
        Order order = Order.draft(this.customerId);
        order.changeShipping(this.shippingInfo, this.shippingCost, this.expectedDeliveryDate);
        order.changeBilling(this.billingInfo);
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
            }
            case CANCELED -> {
            }
        }

        return order;
    }

    public static BillingInfo someBillingInfo() {
        return BillingInfo.builder()
                .address(someAddress())
                .fullName(new FullName("Antony", "Edward Stark"))
                .phone(new Phone("555-111-2222"))
                .document(new Document("0102-03-0405"))
                .build();
    }

    public static ShippingInfo someShippingInfo() {
        return ShippingInfo.builder()
                .address(someAddress())
                .fullName(new FullName("Antony", "Stark"))
                .document(new Document("323-33-2323"))
                .phone(new Phone("555-555-5555"))
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

    public OrderTestDataBuilder customerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderTestDataBuilder paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderTestDataBuilder shippingCost(Money shippingCost) {
        this.shippingCost = shippingCost;
        return this;
    }

    public OrderTestDataBuilder expectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
        return this;
    }

    public OrderTestDataBuilder shippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
        return this;
    }

    public OrderTestDataBuilder billingInfo(BillingInfo billingInfo) {
        this.billingInfo = billingInfo;
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
