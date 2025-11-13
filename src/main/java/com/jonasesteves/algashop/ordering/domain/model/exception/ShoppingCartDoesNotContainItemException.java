package com.jonasesteves.algashop.ordering.domain.model.exception;

import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;

import static com.jonasesteves.algashop.ordering.domain.model.exception.ErrorMessages.SHOPPING_CART_DOES_NOT_CONTAIN_ITEM_ID_ERROR;

public class ShoppingCartDoesNotContainItemException extends DomainException {

    public ShoppingCartDoesNotContainItemException(ShoppingCartId id, ShoppingCartItemId shoppingCartItemId) {
        super(String.format(SHOPPING_CART_DOES_NOT_CONTAIN_ITEM_ID_ERROR, id, shoppingCartItemId));
    }
}
