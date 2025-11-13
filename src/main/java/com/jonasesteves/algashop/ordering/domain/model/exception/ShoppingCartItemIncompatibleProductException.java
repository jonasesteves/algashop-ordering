package com.jonasesteves.algashop.ordering.domain.model.exception;

import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ProductId;

import static com.jonasesteves.algashop.ordering.domain.model.exception.ErrorMessages.SHOPPING_CART_ITEM_PRODUCT_INCOMPATIBLE_ERROR;

public class ShoppingCartItemIncompatibleProductException extends DomainException {
    public ShoppingCartItemIncompatibleProductException(ProductId productId) {
        super(String.format(SHOPPING_CART_ITEM_PRODUCT_INCOMPATIBLE_ERROR, productId));
    }
}
