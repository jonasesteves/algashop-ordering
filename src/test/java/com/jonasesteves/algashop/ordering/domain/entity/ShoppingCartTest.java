package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    @Test
    void shouldGenerateEmptyCart() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Assertions.assertWith(shoppingCart,
                s -> Assertions.assertThat(s.id()).isNotNull(),
                s -> Assertions.assertThat(s.customerId()).isEqualTo(customerId),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(Money.ZERO),
                s -> Assertions.assertThat(s.totalItems()).isEqualTo(Quantity.ZERO),
                s -> Assertions.assertThat(s.createdAt()).isNotNull()
        );
    }

}