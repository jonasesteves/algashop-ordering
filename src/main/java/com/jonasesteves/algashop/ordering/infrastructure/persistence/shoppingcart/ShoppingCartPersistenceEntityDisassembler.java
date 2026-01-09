package com.jonasesteves.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.jonasesteves.algashop.ordering.domain.model.commons.Money;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductName;
import com.jonasesteves.algashop.ordering.domain.model.commons.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductId;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemId;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ShoppingCartPersistenceEntityDisassembler {

    public ShoppingCart toDomainEntity(ShoppingCartPersistenceEntity shoppingCartPersistenceEntity) {
        return ShoppingCart.existing()
                .id(new ShoppingCartId(shoppingCartPersistenceEntity.getId()))
                .customerId(new CustomerId(shoppingCartPersistenceEntity.getCustomerId()))
                .totalAmount(new Money(shoppingCartPersistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(shoppingCartPersistenceEntity.getTotalItems()))
                .createdAt(shoppingCartPersistenceEntity.getCreatedAt())
                .items(toItemsDomainEntities(shoppingCartPersistenceEntity.getItems()))
                .build();
    }

    private Set<ShoppingCartItem> toItemsDomainEntities(Set<ShoppingCartItemPersistenceEntity> itemsSet) {
        return itemsSet.stream().map(this::toItemEntity).collect(Collectors.toSet());
    }

    private ShoppingCartItem toItemEntity(ShoppingCartItemPersistenceEntity item) {
        return ShoppingCartItem.existing()
                .id(new ShoppingCartItemId(item.getId()))
                .shoppingCartId(new ShoppingCartId(item.getShoppingCartId()))
                .productId(new ProductId(item.getProductId()))
                .productName(new ProductName(item.getName()))
                .quantity(new Quantity(item.getQuantity()))
                .price(new Money(item.getPrice()))
                .totalAmount(new Money(item.getTotalAmount()))
                .available(item.getAvailable())
                .build();
    }
}
