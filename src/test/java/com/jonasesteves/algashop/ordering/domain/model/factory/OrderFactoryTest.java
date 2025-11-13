package com.jonasesteves.algashop.ordering.domain.model.factory;

import com.jonasesteves.algashop.ordering.domain.model.entity.Order;
import com.jonasesteves.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.entity.PaymentMethod;
import com.jonasesteves.algashop.ordering.domain.model.entity.ProductTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Billing;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Shipping;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderFactoryTest {

    @Test
    void shouldGenerateFilledOrderThatCanBePlaced() {
        Shipping shipping = OrderTestDataBuilder.someShipping();
        Billing billing = OrderTestDataBuilder.someBilling();

        Product product = ProductTestDataBuilder.someProduct().build();
        PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

        Quantity quantity = new Quantity(1);
        CustomerId customerId = new CustomerId();

        Order order = OrderFactory.filled(customerId, shipping, billing, paymentMethod, product, quantity);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.shipping()).isEqualTo(shipping),
                o -> Assertions.assertThat(o.billing()).isEqualTo(billing),
                o -> Assertions.assertThat(o.paymentMethod()).isEqualTo(paymentMethod),
                o -> Assertions.assertThat(o.items()).isNotEmpty(),
                o -> Assertions.assertThat(o.customerId()).isNotNull(),
                o -> Assertions.assertThat(o.isDraft()).isTrue()
        );

        order.place();

        Assertions.assertThat(order.isPlaced()).isTrue();
    }

}