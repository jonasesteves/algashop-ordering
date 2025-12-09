package com.jonasesteves.algashop.ordering.infrastructure.persistence.provider;

import com.jonasesteves.algashop.ordering.domain.model.entity.Customer;
import com.jonasesteves.algashop.ordering.domain.model.repository.Customers;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerPersistenceProvider implements Customers {

    private final CustomerPersistenceEntityRepository repository;
    private final CustomerPersistenceEntityAssembler assembler;
    private final CustomerPersistenceEntityDisassembler disassembler;

    public CustomerPersistenceProvider(CustomerPersistenceEntityRepository repository, CustomerPersistenceEntityAssembler assembler, CustomerPersistenceEntityDisassembler disassembler) {
        this.repository = repository;
        this.assembler = assembler;
        this.disassembler = disassembler;
    }

    @Override
    public Optional<Customer> ofId(CustomerId customerId) {
        return Optional.empty();
    }

    @Override
    public boolean exists(CustomerId customerId) {
        return false;
    }

    @Override
    public void add(Customer aggregateRoot) {

    }

    @Override
    public long count() {
        return 0;
    }
}
