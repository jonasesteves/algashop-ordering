package com.jonasesteves.algashop.ordering.infrastructure.persistence.customer;

import com.jonasesteves.algashop.ordering.domain.model.customer.Customer;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customers;
import com.jonasesteves.algashop.ordering.domain.model.commons.Email;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class CustomersPersistenceProvider implements Customers {

    private final CustomerPersistenceEntityRepository repository;
    private final CustomerPersistenceEntityAssembler assembler;
    private final CustomerPersistenceEntityDisassembler disassembler;
    private final EntityManager entityManager;

    public CustomersPersistenceProvider(
            CustomerPersistenceEntityRepository repository,
            CustomerPersistenceEntityAssembler assembler,
            CustomerPersistenceEntityDisassembler disassembler,
            EntityManager entityManager) {
        this.repository = repository;
        this.assembler = assembler;
        this.disassembler = disassembler;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Customer> ofId(CustomerId customerId) {
        return repository.findById(customerId.value())
                .map(disassembler::toDomain);
    }

    @Override
    public boolean exists(CustomerId customerId) {
        return repository.existsById(customerId.value());
    }

    @Override
    @Transactional(readOnly = false)
    public void add(Customer aggregateRoot) {
        UUID customerId = aggregateRoot.id().value();
        repository.findById(customerId).ifPresentOrElse(
                customerPersistenceEntity -> update(aggregateRoot, customerPersistenceEntity),
                () -> insert(aggregateRoot)
        );
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public Optional<Customer> ofEmail(Email email) {
        return repository.findByEmail(email.value()).map(disassembler::toDomain);
    }

    @Override
    public boolean isEmailUnique(Email email, CustomerId exceptCustomerId) {
        return !repository.existsByEmailAndIdNot(email.value(), exceptCustomerId.value());
    }

    private void update(Customer aggregateRoot, CustomerPersistenceEntity customerPersistenceEntity) {
        customerPersistenceEntity = assembler.merge(customerPersistenceEntity, aggregateRoot);
        entityManager.detach(customerPersistenceEntity);
        customerPersistenceEntity = repository.saveAndFlush(customerPersistenceEntity);
        updateVersion(aggregateRoot, customerPersistenceEntity);
    }

    private void insert(Customer aggregateRoot) {
        CustomerPersistenceEntity customerPersistenceEntity = assembler.fromDomain(aggregateRoot);
        customerPersistenceEntity = repository.saveAndFlush(customerPersistenceEntity);
        updateVersion(aggregateRoot, customerPersistenceEntity);
    }

    private void updateVersion(Customer aggregateRoot, CustomerPersistenceEntity customerPersistenceEntity) {
        try {
            Field version = aggregateRoot.getClass().getDeclaredField("version");
            version.setAccessible(true);
            ReflectionUtils.setField(version, aggregateRoot, customerPersistenceEntity.getVersion());
            version.setAccessible(false);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Error updating version", e);
        }
    }
}
