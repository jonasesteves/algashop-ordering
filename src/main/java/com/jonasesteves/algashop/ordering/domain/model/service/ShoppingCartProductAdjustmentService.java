package com.jonasesteves.algashop.ordering.domain.model.service;

import com.jonasesteves.algashop.ordering.domain.model.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ProductId;

public interface ShoppingCartProductAdjustmentService {
    void adjustPrice(ProductId productId, Money updatedPrice);
    void changeAvailability(ProductId productId, boolean available);
}
