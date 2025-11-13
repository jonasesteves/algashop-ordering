package com.jonasesteves.algashop.ordering.domain.model.repository;

import com.jonasesteves.algashop.ordering.domain.model.entity.Customer;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;

public interface Customers extends Repository<Customer, CustomerId> {
}
