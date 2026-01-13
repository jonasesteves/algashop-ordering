package com.jonasesteves.algashop.ordering.infrastructure.product.client;

import com.jonasesteves.algashop.ordering.domain.model.product.ProductCatalogService;
import com.jonasesteves.algashop.ordering.domain.model.commons.Money;
import com.jonasesteves.algashop.ordering.domain.model.product.Product;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductName;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
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
