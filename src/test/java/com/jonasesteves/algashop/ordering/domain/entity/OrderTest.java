package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.valueobject.ProductName;
import com.jonasesteves.algashop.ordering.domain.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class OrderTest {

    @Test
    public void shouldGenerate() {
        Order.draft(new CustomerId());
    }

    @Test
    void shouldAddItem() {
        Order order = Order.draft(new CustomerId());
        ProductId productId = new ProductId();

        order.addItem(
                productId,
                new ProductName("Chocolate"),
                new Money("0.99"),
                new Quantity(2)
        );

        OrderItem item = order.items().iterator().next();

        Assertions.assertThat(order.items().size()).isEqualTo(1);
        Assertions.assertWith(item,
            i -> Assertions.assertThat(i.id()).isNotNull(),
                i -> Assertions.assertThat(i.productName().value()).isEqualTo("Chocolate"),
                i -> Assertions.assertThat(i.productId().value()).isEqualTo(productId.value()),
                i -> Assertions.assertThat(i.price().value()).hasToString("0.99"),
                i -> Assertions.assertThat(i.quantity().value()).isEqualTo(2)
        );
    }

    @Test
    void givenAnItemsSet_whenTryToChange_thenGenerateException() {
        Order order = Order.draft(new CustomerId());
        ProductId productId = new ProductId();

        order.addItem(
                productId,
                new ProductName("Chocolate"),
                new Money("0.99"),
                new Quantity(2)
        );

        Set<OrderItem> items = order.items();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy((items::clear));
    }

    @Test
    void shouldRecalculateTotals() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                new ProductId(),
                new ProductName("Chocolate"),
                new Money("0.99"),
                new Quantity(2)
        );

        order.addItem(
                new ProductId(),
                new ProductName("Pizza"),
                new Money("2.50"),
                new Quantity(4)
        );

        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("11.98"));
        Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(6));
    }

}