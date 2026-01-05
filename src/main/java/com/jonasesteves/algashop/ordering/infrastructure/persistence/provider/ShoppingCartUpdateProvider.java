package com.jonasesteves.algashop.ordering.infrastructure.persistence.provider;

import com.jonasesteves.algashop.ordering.domain.model.service.ShoppingCartProductAdjustmentService;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ShoppingCartUpdateProvider implements ShoppingCartProductAdjustmentService {

    private final ShoppingCartPersistenceEntityRepository repository;

    public ShoppingCartUpdateProvider(ShoppingCartPersistenceEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void adjustPrice(ProductId productId, Money updatedPrice) {
        repository.updateItemPrice(productId.value(), updatedPrice.value());
        repository.recalculateTotalsForCartsWithProduct(productId.value());
    }

    @Override
    @Transactional
    public void changeAvailability(ProductId productId, boolean available) {
        repository.updateItemAvailability(productId.value(), available);
    }
}
