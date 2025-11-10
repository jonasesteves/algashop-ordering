package com.jonasesteves.algashop.ordering.domain.exception;

import com.jonasesteves.algashop.ordering.domain.valueobject.id.ProductId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ShoppingCartId;

import static com.jonasesteves.algashop.ordering.domain.exception.ErrorMessages.SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT_ID_ERROR;

public class ShoppingCartDoesNotContainProductException extends DomainException {
    public ShoppingCartDoesNotContainProductException(ShoppingCartId id, ProductId productId) {
        super(String.format(SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT_ID_ERROR, id, productId));
    }
}
