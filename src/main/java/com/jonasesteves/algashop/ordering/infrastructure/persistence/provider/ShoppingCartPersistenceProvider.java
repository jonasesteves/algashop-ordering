package com.jonasesteves.algashop.ordering.infrastructure.persistence.provider;

import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.repository.ShoppingCarts;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceEntityDisassembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class ShoppingCartPersistenceProvider implements ShoppingCarts {

    private final ShoppingCartPersistenceEntityRepository repository;
    private final ShoppingCartPersistenceEntityDisassembler disassembler;

    public ShoppingCartPersistenceProvider(ShoppingCartPersistenceEntityRepository repository, ShoppingCartPersistenceEntityDisassembler disassembler) {
        this.repository = repository;
        this.disassembler = disassembler;
    }

    @Override
    public Optional<ShoppingCart> ofCustomer(CustomerId customerId) {
        return Optional.empty();
    }

    @Override
    public void remove(ShoppingCart shoppingCart) {

    }

    @Override
    public void remove(ShoppingCartId shoppingCartId) {

    }

    @Override
    public Optional<ShoppingCart> ofId(ShoppingCartId shoppingCartId) {
        Optional<ShoppingCartPersistenceEntity> possibleEntity = repository.findById(shoppingCartId.value());
        return possibleEntity.map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(ShoppingCartId shoppingCartId) {
        return false;
    }

    @Override
    public void add(ShoppingCart aggregateRoot) {

    }

    @Override
    public long count() {
        return 0;
    }
}
