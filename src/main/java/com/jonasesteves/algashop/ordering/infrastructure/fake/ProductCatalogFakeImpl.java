package com.jonasesteves.algashop.ordering.infrastructure.fake;

import com.jonasesteves.algashop.ordering.domain.model.service.ProductCatalogService;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.ProductName;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ProductId;

import java.util.Optional;

public class ProductCatalogFakeImpl implements ProductCatalogService {
    @Override
    public Optional<Product> ofId(ProductId productId) {
        Product product = Product.builder().id(productId)
                .name(new ProductName("Notebook"))
                .price(new Money("7000"))
                .build();
        return Optional.of(product);
    }
}
