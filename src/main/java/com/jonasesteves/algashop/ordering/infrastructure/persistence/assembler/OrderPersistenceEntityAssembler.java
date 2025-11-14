package com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler;

import com.jonasesteves.algashop.ordering.domain.model.entity.Order;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;

public class OrderPersistenceEntityAssembler {

    public OrderPersistenceEntity fromDomain(Order order) {
        return merge(new OrderPersistenceEntity(), order);
    }

    public OrderPersistenceEntity merge (OrderPersistenceEntity orderPersistenceEntity, Order order) {

        return null;
    }
}
