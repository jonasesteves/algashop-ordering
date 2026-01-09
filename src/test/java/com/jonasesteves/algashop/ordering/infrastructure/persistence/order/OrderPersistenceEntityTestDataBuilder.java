package com.jonasesteves.algashop.ordering.infrastructure.persistence.order;

import com.jonasesteves.algashop.ordering.domain.model.IdGenerator;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityTestDataBuilder;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntity.OrderPersistenceEntityBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

public class OrderPersistenceEntityTestDataBuilder {

    private OrderPersistenceEntityTestDataBuilder() {
    }

    public static OrderPersistenceEntityBuilder existingOrder() {
        return OrderPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customer(CustomerPersistenceEntityTestDataBuilder.someCustomer().build())
                .totalItems(3)
                .totalAmount(new BigDecimal(1050))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now())
                .items(Set.of(existingItem().build(), existingItemAlt().build()));
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingItem() {
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .price(new BigDecimal(500))
                .quantity(2)
                .totalAmount(new BigDecimal(1000))
                .productName("RAM Memory")
                .productId(IdGenerator.generateTimeBasedUUID());
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingItemAlt() {
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .price(new BigDecimal(50))
                .quantity(1)
                .totalAmount(new BigDecimal(50))
                .productName("Keyboard")
                .productId(IdGenerator.generateTimeBasedUUID());
    }
}
