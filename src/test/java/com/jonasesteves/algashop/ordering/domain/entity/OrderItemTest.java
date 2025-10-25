package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.valueobject.ProductName;
import com.jonasesteves.algashop.ordering.domain.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.OrderId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ProductId;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void shouldGenerate() {
        OrderItem.brandNew()
                .orderId(new OrderId())
                .productId(new ProductId())
                .productName(new ProductName("Chocolate"))
                .quantity(new Quantity(2))
                .price(new Money("0.99"))
                .build();
    }

}