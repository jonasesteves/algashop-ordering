package com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler;

import com.jonasesteves.algashop.ordering.domain.model.entity.Order;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Address;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Billing;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Recipient;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Shipping;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistenceEntityAssembler {

    public OrderPersistenceEntity fromDomain(Order order) {
        return merge(new OrderPersistenceEntity(), order);
    }

    public OrderPersistenceEntity merge(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        orderPersistenceEntity.setId(order.id().value().toLong());
        orderPersistenceEntity.setCustomerId(order.customerId().value());
        orderPersistenceEntity.setTotalAmount(order.totalAmount().value());
        orderPersistenceEntity.setTotalItems(order.totalItems().value());
        orderPersistenceEntity.setStatus(order.status().name());
        orderPersistenceEntity.setPaymentMethod(order.paymentMethod().name());
        orderPersistenceEntity.setPlacedAt(order.placedAt());
        orderPersistenceEntity.setPaidAt(order.paidAt());
        orderPersistenceEntity.setCanceledAt(order.canceledAt());
        orderPersistenceEntity.setReadyAt(order.readyAt());
        orderPersistenceEntity.setVersion(order.version());
        orderPersistenceEntity.setBilling(this.billingEmbeddable(order.billing()));
        orderPersistenceEntity.setShipping(this.shippingEmbeddable(order.shipping()));
        return orderPersistenceEntity;
    }

    private BillingEmbeddable billingEmbeddable(Billing billing) {
        if (billing == null) return null;

        return new BillingEmbeddable(
                billing.fullName().firstName(),
                billing.fullName().lastName(),
                billing.document().value(),
                billing.phone().value(),
                billing.email().value(),
                this.addressEmbeddable(billing.address())
        );
    }

    private ShippingEmbeddable shippingEmbeddable(Shipping shipping) {
        if (shipping == null) return null;

        return new ShippingEmbeddable(
                shipping.cost().value(),
                shipping.expectedDate(),
                this.recipientEmbeddable(shipping.recipient()),
                this.addressEmbeddable(shipping.address())
        );
    }

    private RecipientEmbeddable recipientEmbeddable(Recipient recipient) {
        if (recipient == null) return null;

        return new RecipientEmbeddable(
                recipient.fullName().firstName(),
                recipient.fullName().lastName(),
                recipient.document().value(),
                recipient.phone().value()
        );
    }

    private AddressEmbeddable addressEmbeddable(Address address) {
        if (address == null) return null;

        return new AddressEmbeddable(
                address.number(),
                address.street(),
                address.complement(),
                address.neighborhood(),
                address.city(),
                address.state(),
                address.zipCode().value()
        );
    }
}
