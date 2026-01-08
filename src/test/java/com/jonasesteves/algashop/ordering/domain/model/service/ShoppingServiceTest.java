package com.jonasesteves.algashop.ordering.domain.model.service;

import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCartTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.exception.CustomerAlreadyHaveShoppingCartException;
import com.jonasesteves.algashop.ordering.domain.model.exception.CustomerNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.repository.Customers;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.provider.ShoppingCartsPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ShoppingServiceTest {

    @Mock
    private Customers customers;

    @Mock
    private ShoppingCartsPersistenceProvider shoppingCartsPersistenceProvider;

    @InjectMocks
    private ShoppingService shoppingService;

    @Test
    void shouldRegister() {
        CustomerId customerId = new CustomerId();

        Mockito.when(customers.exists(Mockito.any(CustomerId.class))).thenReturn(true);
        Mockito.when(shoppingCartsPersistenceProvider.ofCustomer(Mockito.any(CustomerId.class))).thenReturn(Optional.empty());

        ShoppingCart shoppingCart = shoppingService.startShopping(customerId);

        Assertions.assertWith(shoppingCart,
                s -> Assertions.assertThat(s.customerId()).isEqualTo(customerId),
                s -> Assertions.assertThat(s.isEmpty()).isTrue(),
                s -> Assertions.assertThat(s.totalItems()).isEqualTo(Quantity.ZERO),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(Money.ZERO)
        );

        Mockito.verify(customers).exists(customerId);
        Mockito.verify(shoppingCartsPersistenceProvider).ofCustomer(customerId);
    }

    @Test
    void givenUnexistentCustomer_whenStartShopping_shouldGenerateException() {
        CustomerId customerId = new CustomerId();

        Mockito.when(customers.exists(Mockito.any(CustomerId.class))).thenReturn(false);

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class).isThrownBy(
                () -> shoppingService.startShopping(customerId)
        );
        Mockito.verify(customers).exists(customerId);
        Mockito.verify(shoppingCartsPersistenceProvider, Mockito.never()).ofCustomer(customerId);
    }

    @Test
    void givenCustomerWithExistentCart_whenStartShopping_shouldGenerateException() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();

        Mockito.when(customers.exists(Mockito.any(CustomerId.class))).thenReturn(true);
        Mockito.when(shoppingCartsPersistenceProvider.ofCustomer(Mockito.any(CustomerId.class))).thenReturn(Optional.of(shoppingCart));

        Assertions.assertThatExceptionOfType(CustomerAlreadyHaveShoppingCartException.class).isThrownBy(
                () -> shoppingService.startShopping(customerId)
        );

        Mockito.verify(customers).exists(customerId);
        Mockito.verify(shoppingCartsPersistenceProvider).ofCustomer(customerId);
    }
}