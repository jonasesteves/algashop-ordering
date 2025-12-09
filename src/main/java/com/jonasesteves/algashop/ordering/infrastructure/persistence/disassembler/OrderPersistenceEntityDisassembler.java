package com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler;

import com.jonasesteves.algashop.ordering.domain.model.entity.Order;
import com.jonasesteves.algashop.ordering.domain.model.entity.OrderItem;
import com.jonasesteves.algashop.ordering.domain.model.entity.OrderStatus;
import com.jonasesteves.algashop.ordering.domain.model.entity.PaymentMethod;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.*;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderPersistenceEntityDisassembler {

    public Order toDomainEntity(OrderPersistenceEntity orderPersistenceEntity) {
        return Order.existing()
                .id(new OrderId(orderPersistenceEntity.getId()))
                .customerId(new CustomerId(orderPersistenceEntity.getCustomerId()))
                .totalAmount(new Money(orderPersistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(orderPersistenceEntity.getTotalItems()))
                .status(OrderStatus.valueOf(orderPersistenceEntity.getStatus()))
                .paymentMethod(PaymentMethod.valueOf(orderPersistenceEntity.getPaymentMethod()))
                .placedAt(orderPersistenceEntity.getPlacedAt())
                .paidAt(orderPersistenceEntity.getPaidAt())
                .canceledAt(orderPersistenceEntity.getCanceledAt())
                .readyAt(orderPersistenceEntity.getReadyAt())
                .items(this.items(orderPersistenceEntity.getItems()))
                .version(orderPersistenceEntity.getVersion())
                .billing(this.billing(orderPersistenceEntity.getBilling()))
                .shipping(this.shipping(orderPersistenceEntity.getShipping()))
                .build();
    }

    private Set<OrderItem> items(Set<OrderItemPersistenceEntity> orderItemsPersistenceEntity) {
        if (orderItemsPersistenceEntity == null || orderItemsPersistenceEntity.isEmpty()) return new HashSet<>();

        return orderItemsPersistenceEntity.stream()
                .map(item -> OrderItem.existing()
                        .id(new OrderItemId(item.getId()))
                        .orderId(new OrderId(item.getOrderId()))
                        .productId(new ProductId(item.getProductId()))
                        .productName(new ProductName(item.getProductName()))
                        .price(new Money(item.getPrice()))
                        .quantity(new Quantity(item.getQuantity()))
                        .totalAmount(new Money(item.getTotalAmount()))
                        .build())
                .collect(Collectors.toSet());
    }

    private Billing billing(BillingEmbeddable billingEmbeddable) {
        if (billingEmbeddable == null) return null;

        return Billing.builder()
                .fullName(new FullName(billingEmbeddable.getFirstName(), billingEmbeddable.getLastName()))
                .document(new Document(billingEmbeddable.getDocument()))
                .phone(new Phone(billingEmbeddable.getPhone()))
                .email(new Email(billingEmbeddable.getEmail()))
                .address(this.address(billingEmbeddable.getAddress()))
                .build();
    }

    private Shipping shipping(ShippingEmbeddable shippingEmbeddable) {
        if (shippingEmbeddable == null) return null;

        return Shipping.builder()
                .cost(new Money(shippingEmbeddable.getCost()))
                .expectedDate(shippingEmbeddable.getExpectedDate())
                .recipient(this.recipient(shippingEmbeddable.getRecipient()))
                .address(this.address(shippingEmbeddable.getAddress()))
                .build();
    }

    private Address address(AddressEmbeddable addressEmbeddable) {
        if (addressEmbeddable == null) return null;

        return Address.builder()
                .number(addressEmbeddable.getNumber())
                .street(addressEmbeddable.getStreet())
                .complement(addressEmbeddable.getComplement())
                .neighborhood(addressEmbeddable.getNeighborhood())
                .city(addressEmbeddable.getCity())
                .state(addressEmbeddable.getState())
                .zipCode(new ZipCode(addressEmbeddable.getZipCode()))
                .build();
    }

    private Recipient recipient(RecipientEmbeddable recipientEmbeddable) {
        if (recipientEmbeddable == null) return null;

        return Recipient.builder()
                .fullName(new FullName(recipientEmbeddable.getFirstName(), recipientEmbeddable.getLastName()))
                .document(new Document(recipientEmbeddable.getDocument()))
                .phone(new Phone(recipientEmbeddable.getPhone()))
                .build();
    }
}
