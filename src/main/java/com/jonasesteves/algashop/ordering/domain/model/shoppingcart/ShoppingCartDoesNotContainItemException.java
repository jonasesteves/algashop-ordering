package com.jonasesteves.algashop.ordering.domain.model.shoppingcart;

import com.jonasesteves.algashop.ordering.domain.model.DomainException;

import static com.jonasesteves.algashop.ordering.domain.model.ErrorMessages.SHOPPING_CART_DOES_NOT_CONTAIN_ITEM_ID_ERROR;

public class ShoppingCartDoesNotContainItemException extends DomainException {

    public ShoppingCartDoesNotContainItemException(ShoppingCartId id, ShoppingCartItemId shoppingCartItemId) {
        super(String.format(SHOPPING_CART_DOES_NOT_CONTAIN_ITEM_ID_ERROR, id, shoppingCartItemId));
    }
}
