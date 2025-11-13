package com.jonasesteves.algashop.ordering.domain.model.entity;

import com.jonasesteves.algashop.ordering.domain.model.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.OrderId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void shouldGenerateBrandNewOrderItem() {
        Product product = ProductTestDataBuilder.someProduct().build();
        Quantity quantity = new Quantity(2);
        OrderId orderId = new OrderId();

        OrderItem orderItem = OrderItem.brandNew()
                .orderId(orderId)
                .product(product)
                .quantity(quantity)
                .build();

        Assertions.assertWith(orderItem,
                o -> Assertions.assertThat(o.id()).isNotNull(),
                o -> Assertions.assertThat(o.orderId()).isEqualTo(orderId),
                o -> Assertions.assertThat(o.productId()).isEqualTo(product.id()),
                o -> Assertions.assertThat(o.productName()).isEqualTo(product.name()),
                o -> Assertions.assertThat(o.price()).isEqualTo(product.price()),
                o -> Assertions.assertThat(o.quantity()).isEqualTo(quantity)
        );

        Assertions.assertThat(orderItem.totalAmount()).isEqualTo(new Money("6000.00"));
    }

}