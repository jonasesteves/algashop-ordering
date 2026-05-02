package com.jonasesteves.algashop.ordering.infrastructure.persistence.customer;

import com.jonasesteves.algashop.ordering.application.customer.query.CustomerOutput;
import com.jonasesteves.algashop.ordering.application.customer.query.CustomerQueryService;
import com.jonasesteves.algashop.ordering.application.utility.Mapper;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerQueryServiceImpl implements CustomerQueryService {

    private final CustomerPersistenceEntityRepository repository;
    private final Mapper mapper;

    public CustomerQueryServiceImpl(CustomerPersistenceEntityRepository repository, Mapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CustomerOutput findById(UUID customerId) {
        CustomerPersistenceEntity customer = repository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        return mapper.convert(customer, CustomerOutput.class);
    }
}
