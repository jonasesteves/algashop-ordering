package com.jonasesteves.algashop.ordering.infrastructure.persistence.provider;

import com.jonasesteves.algashop.ordering.domain.model.entity.Order;
import com.jonasesteves.algashop.ordering.domain.model.repository.Orders;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.assimbler.OrderPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceEntityRepository orderPersistenceEntityRepository;
    private final OrderPersistenceEntityAssembler assembler;

    public OrdersPersistenceProvider(OrderPersistenceEntityRepository orderPersistenceEntityRepository, OrderPersistenceEntityAssembler assembler) {
        this.orderPersistenceEntityRepository = orderPersistenceEntityRepository;
        this.assembler = assembler;
    }

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        return Optional.empty();
    }

    @Override
    public boolean exists(OrderId orderId) {
        return false;
    }

    @Override
    public void add(Order aggregateRoot) {
        OrderPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);
        orderPersistenceEntityRepository.saveAndFlush(persistenceEntity);
    }

    @Override
    public int count() {
        return 0;
    }
}
