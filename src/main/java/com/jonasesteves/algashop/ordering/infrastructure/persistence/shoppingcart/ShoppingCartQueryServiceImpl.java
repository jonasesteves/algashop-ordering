package com.jonasesteves.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.jonasesteves.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.jonasesteves.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.jonasesteves.algashop.ordering.application.utility.Mapper;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ShoppingCartQueryServiceImpl implements ShoppingCartQueryService {

    private final ShoppingCartPersistenceEntityRepository persistenceRepository;
    private final Mapper mapper;

    public ShoppingCartQueryServiceImpl(ShoppingCartPersistenceEntityRepository persistenceRepository, Mapper mapper) {
        this.persistenceRepository = persistenceRepository;
        this.mapper = mapper;
    }

    @Override
    public ShoppingCartOutput findById(UUID shoppingCartId) {
        return persistenceRepository.findById(shoppingCartId)
                .map(s -> mapper.convert(s, ShoppingCartOutput.class))
                .orElseThrow(ShoppingCartNotFoundException::new);
    }

    @Override
    public ShoppingCartOutput findByCustomerId(UUID customerId) {
        return persistenceRepository.findByCustomer_Id(customerId)
                .map(s -> mapper.convert(s, ShoppingCartOutput.class))
                .orElseThrow(ShoppingCartNotFoundException::new);
    }
}
