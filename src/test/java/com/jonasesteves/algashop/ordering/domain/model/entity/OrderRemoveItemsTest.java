package com.jonasesteves.algashop.ordering.domain.model.entity;

import com.jonasesteves.algashop.ordering.domain.model.exception.OrderCannotBeChangedException;
import com.jonasesteves.algashop.ordering.domain.model.exception.OrderDoesNotContainItemException;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderRemoveItemsTest {

    @Test
    void givenOrderWithItems_whenRemoveItem_shouldRemoveCorrectly() {
        Order order = Order.draft(new CustomerId());
        order.addItem(
                ProductTestDataBuilder.someProduct().build(),
                new Quantity(2)
        );

        OrderItem item = order.items().iterator().next();

        order.addItem(
                ProductTestDataBuilder.chocolate().build(),
                new Quantity(3)
        );

        order.removeItem(item.id());

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.items()).hasSize(1),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(new Quantity(3)),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("2.97"))
        );
    }

    @Test
    void givenOrder_whenRemoveInexistentItem_shouldGenerateException() {
        Order order = OrderTestDataBuilder.someOrder().build();
        OrderItemId itemId = new OrderItemId();

        Assertions.assertThatExceptionOfType(OrderDoesNotContainItemException.class).isThrownBy(() -> order.removeItem(itemId));

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.items()).hasSize(2),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("12012.97"))
        );
    }

    @Test
    void givenPlacedOrder_whenRemoveItem_shouldGenerageException() {
        Order order = OrderTestDataBuilder.someOrder().build();
        OrderItemId itemId = new OrderItemId();
        order.place();

        Assertions.assertThatExceptionOfType(OrderCannotBeChangedException.class).isThrownBy(() -> order.removeItem(itemId));

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.items()).hasSize(2),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("12012.97"))
        );
    }
}
