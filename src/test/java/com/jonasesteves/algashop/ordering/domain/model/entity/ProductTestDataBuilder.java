package com.jonasesteves.algashop.ordering.domain.model.entity;

import com.jonasesteves.algashop.ordering.domain.model.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.ProductName;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ProductId;

public class ProductTestDataBuilder {

    public static final ProductId DEFAULT_PRODUCT_ID = new ProductId();

    private ProductTestDataBuilder() {}

    public static Product.ProductBuilder someProduct() {
        return Product.builder()
                .id(DEFAULT_PRODUCT_ID)
                .name(new ProductName("Adamantium"))
                .price(new Money("3000.00"))
                .inStock(true);
    }

    public static Product.ProductBuilder someUnavailableProduct() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("Kriptonita"))
                .price(new Money("30000.00"))
                .inStock(false);
    }

    public static Product.ProductBuilder someProductAlt() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("Chocolate"))
                .price(new Money("0.99"))
                .inStock(true);
    }
}
