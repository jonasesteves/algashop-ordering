package com.jonasesteves.algashop.ordering.domain.model.shoppingcart;

import com.jonasesteves.algashop.ordering.domain.model.DomainException;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductId;

import static com.jonasesteves.algashop.ordering.domain.model.ErrorMessages.SHOPPING_CART_ITEM_PRODUCT_INCOMPATIBLE_ERROR;

public class ShoppingCartItemIncompatibleProductException extends DomainException {
    public ShoppingCartItemIncompatibleProductException(ProductId productId) {
        super(String.format(SHOPPING_CART_ITEM_PRODUCT_INCOMPATIBLE_ERROR, productId));
    }
}
