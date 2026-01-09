package com.jonasesteves.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class ShoppingCartsPersistenceProvider implements ShoppingCarts {

    private final ShoppingCartPersistenceEntityRepository repository;
    private final ShoppingCartPersistenceEntityAssembler assembler;
    private final ShoppingCartPersistenceEntityDisassembler disassembler;
    private final EntityManager entityManager;

    public ShoppingCartsPersistenceProvider(ShoppingCartPersistenceEntityRepository repository, ShoppingCartPersistenceEntityAssembler assembler, ShoppingCartPersistenceEntityDisassembler disassembler, EntityManager entityManager) {
        this.repository = repository;
        this.assembler = assembler;
        this.disassembler = disassembler;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<ShoppingCart> ofCustomer(CustomerId customerId) {
        Optional<ShoppingCartPersistenceEntity> possibleEntity = repository.findByCustomer_Id(customerId.value());
        return possibleEntity.map(disassembler::toDomainEntity);
    }

    @Override
    @Transactional(readOnly = false)
    public void remove(ShoppingCart shoppingCart) {
        repository.deleteById(shoppingCart.id().value());
    }

    @Override
    @Transactional(readOnly = false)
    public void remove(ShoppingCartId shoppingCartId) {
        repository.deleteById(shoppingCartId.value());
    }

    @Override
    public Optional<ShoppingCart> ofId(ShoppingCartId shoppingCartId) {
        Optional<ShoppingCartPersistenceEntity> possibleEntity = repository.findById(shoppingCartId.value());
        return possibleEntity.map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(ShoppingCartId shoppingCartId) {
        return repository.existsById(shoppingCartId.value());
    }

    @Override
    @Transactional(readOnly = true)
    public void add(ShoppingCart aggregateRoot) {
        UUID shoppingCartId = aggregateRoot.id().value();
        repository.findById(shoppingCartId).ifPresentOrElse(
                shoppingCartPersistenceEntity -> update(aggregateRoot, shoppingCartPersistenceEntity),
                () -> insert(aggregateRoot)
        );
    }

    @Override
    public long count() {
        return repository.count();
    }

    private void update(ShoppingCart aggregateRoot, ShoppingCartPersistenceEntity shoppingCartPersistenceEntity) {
        shoppingCartPersistenceEntity = assembler.merge(shoppingCartPersistenceEntity, aggregateRoot);
        entityManager.detach(shoppingCartPersistenceEntity);
        shoppingCartPersistenceEntity = repository.saveAndFlush(shoppingCartPersistenceEntity);
        updateVersion(aggregateRoot, shoppingCartPersistenceEntity);
    }

    private void insert(ShoppingCart aggregateRoot) {
        ShoppingCartPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);
        repository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot, persistenceEntity);
    }

    private void updateVersion(ShoppingCart aggregateRoot, ShoppingCartPersistenceEntity shoppingCartPersistenceEntity) {
        try {
            Field version = aggregateRoot.getClass().getDeclaredField("version");
            version.setAccessible(true);
            ReflectionUtils.setField(version, aggregateRoot, shoppingCartPersistenceEntity.getVersion());
            version.setAccessible(false);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Error updating version", e);
        }
    }
}
