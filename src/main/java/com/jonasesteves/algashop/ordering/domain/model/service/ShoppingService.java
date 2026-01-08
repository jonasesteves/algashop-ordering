package com.jonasesteves.algashop.ordering.domain.model.service;

import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.exception.CustomerAlreadyHaveShoppingCartException;
import com.jonasesteves.algashop.ordering.domain.model.exception.CustomerNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.repository.Customers;
import com.jonasesteves.algashop.ordering.domain.model.utility.DomainService;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.provider.ShoppingCartsPersistenceProvider;

@DomainService
public class ShoppingService {

    private final Customers customers;
    private final ShoppingCartsPersistenceProvider shoppingCartsPersistenceProvider;

    public ShoppingService(Customers customers, ShoppingCartsPersistenceProvider shoppingCartsPersistenceProvider) {
        this.customers = customers;
        this.shoppingCartsPersistenceProvider = shoppingCartsPersistenceProvider;
    }

    public ShoppingCart startShopping(CustomerId customerId) {
        if (!customers.exists(customerId)) {
            throw new CustomerNotFoundException();
        }

        if (shoppingCartsPersistenceProvider.ofCustomer(customerId).isPresent()) {
            throw new CustomerAlreadyHaveShoppingCartException();
        }

        return ShoppingCart.startShopping(customerId);
    }
}
