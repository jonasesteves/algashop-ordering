package com.jonasesteves.algashop.ordering.domain.exception;

import com.jonasesteves.algashop.ordering.domain.valueobject.id.ProductId;

import static com.jonasesteves.algashop.ordering.domain.exception.ErrorMessages.SHOPPING_CART_ITEM_PRODUCT_INCOMPATIBLE_ERROR;

public class ShoppingCartItemIncompatibleProductException extends DomainException {
    public ShoppingCartItemIncompatibleProductException(ProductId productId) {
        super(String.format(SHOPPING_CART_ITEM_PRODUCT_INCOMPATIBLE_ERROR, productId));
    }
}
