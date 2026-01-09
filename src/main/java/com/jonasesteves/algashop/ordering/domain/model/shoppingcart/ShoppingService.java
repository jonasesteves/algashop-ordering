package com.jonasesteves.algashop.ordering.domain.model.shoppingcart;

import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customers;
import com.jonasesteves.algashop.ordering.domain.model.DomainService;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartsPersistenceProvider;

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
