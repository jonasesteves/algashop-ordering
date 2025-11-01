package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.valueobject.ProductName;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ProductId;

public class ProductTestDataBuilder {

    private ProductTestDataBuilder() {}

    public static Product.ProductBuilder someProduct() {
        return Product.builder()
                .id(new ProductId())
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

    public static Product.ProductBuilder chocolate() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("Chocolate"))
                .price(new Money("0.99"))
                .inStock(true);
    }
}
