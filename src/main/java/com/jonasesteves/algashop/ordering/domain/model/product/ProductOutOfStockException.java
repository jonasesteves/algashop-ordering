package com.jonasesteves.algashop.ordering.domain.model.product;

import com.jonasesteves.algashop.ordering.domain.model.DomainException;

import static com.jonasesteves.algashop.ordering.domain.model.ErrorMessages.PRODUCT_OUT_OF_STOCK_ERROR;

public class ProductOutOfStockException extends DomainException {
    public ProductOutOfStockException(ProductId id) {
        super(String.format(PRODUCT_OUT_OF_STOCK_ERROR, id));
    }
}
