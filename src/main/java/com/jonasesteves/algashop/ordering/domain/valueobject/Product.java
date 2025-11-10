package com.jonasesteves.algashop.ordering.domain.valueobject;

import com.jonasesteves.algashop.ordering.domain.exception.ProductOutOfStockException;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ProductId;
import lombok.Builder;

import java.util.Objects;

@Builder
public record Product(ProductId id, ProductName name, Money price, Boolean inStock) {

    public Product {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(price);
        Objects.requireNonNull(inStock);
    }

    public void checkOutOfStock() {
        if (Boolean.FALSE.equals(inStock())) {
            throw new ProductOutOfStockException(this.id());
        }
    }
}
