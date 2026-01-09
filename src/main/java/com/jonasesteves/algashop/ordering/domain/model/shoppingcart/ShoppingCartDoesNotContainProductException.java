package com.jonasesteves.algashop.ordering.domain.model.shoppingcart;

import com.jonasesteves.algashop.ordering.domain.model.DomainException;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductId;

import static com.jonasesteves.algashop.ordering.domain.model.ErrorMessages.SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT_ID_ERROR;

public class ShoppingCartDoesNotContainProductException extends DomainException {
    public ShoppingCartDoesNotContainProductException(ShoppingCartId id, ProductId productId) {
        super(String.format(SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT_ID_ERROR, id, productId));
    }
}
