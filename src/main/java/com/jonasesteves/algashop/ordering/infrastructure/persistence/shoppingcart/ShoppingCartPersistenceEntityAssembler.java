package com.jonasesteves.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ShoppingCartPersistenceEntityAssembler {

    private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    public ShoppingCartPersistenceEntityAssembler(CustomerPersistenceEntityRepository customerPersistenceEntityRepository) {
        this.customerPersistenceEntityRepository = customerPersistenceEntityRepository;
    }

    public ShoppingCartPersistenceEntity fromDomain(ShoppingCart shoppingCart) {
        return merge(new ShoppingCartPersistenceEntity(), shoppingCart);
    }

    public ShoppingCartPersistenceEntity merge(ShoppingCartPersistenceEntity shoppingCartPersistenceEntity, ShoppingCart shoppingCart) {
        shoppingCartPersistenceEntity.setId(shoppingCart.id().value());
        shoppingCartPersistenceEntity.setTotalAmount(shoppingCart.totalAmount().value());
        shoppingCartPersistenceEntity.setTotalItems(shoppingCart.totalItems().value());
        shoppingCartPersistenceEntity.setVersion(shoppingCart.version());

        Set<ShoppingCartItemPersistenceEntity> mergedItems = mergeItems(shoppingCartPersistenceEntity, shoppingCart);
        shoppingCartPersistenceEntity.replaceItems(mergedItems);

        var customerPersistenceEntity = customerPersistenceEntityRepository.getReferenceById(shoppingCart.customerId().value());
        shoppingCartPersistenceEntity.setCustomer(customerPersistenceEntity);

        return shoppingCartPersistenceEntity;
    }

    private Set<ShoppingCartItemPersistenceEntity> mergeItems(ShoppingCartPersistenceEntity shoppingCartPersistenceEntity, ShoppingCart shoppingCart) {
        Set<ShoppingCartItem> newOrUpdatedItems = shoppingCart.items();

        if (newOrUpdatedItems == null || newOrUpdatedItems.isEmpty()) {
            return new HashSet<>();
        }

        Set<ShoppingCartItemPersistenceEntity> existingItems = shoppingCartPersistenceEntity.getItems();
        if (existingItems == null || existingItems.isEmpty()) {
            return newOrUpdatedItems.stream().map(this::fromDomain).collect(Collectors.toSet());
        }

        Map<UUID, ShoppingCartItemPersistenceEntity> existingItemMap = existingItems.stream()
                .collect(Collectors.toMap(ShoppingCartItemPersistenceEntity::getId, s -> s));

        return newOrUpdatedItems.stream().map(
                shoppingCartItem -> {
                    ShoppingCartItemPersistenceEntity itemPersistence = existingItemMap.getOrDefault(
                            shoppingCartItem.id().value(), new ShoppingCartItemPersistenceEntity()
                    );
                    return merge(itemPersistence, shoppingCartItem);
                }
        ).collect(Collectors.toSet());
    }

    public ShoppingCartItemPersistenceEntity fromDomain(ShoppingCartItem shoppingCartItem) {
        return merge(new ShoppingCartItemPersistenceEntity(), shoppingCartItem);
    }

    private ShoppingCartItemPersistenceEntity merge(ShoppingCartItemPersistenceEntity shoppingCartItemPersistenceEntity, ShoppingCartItem shoppingCartItem) {
        shoppingCartItemPersistenceEntity.setId(shoppingCartItem.id().value());
        shoppingCartItemPersistenceEntity.setProductId(shoppingCartItem.productId().value());
        shoppingCartItemPersistenceEntity.setName(shoppingCartItem.name().value());
        shoppingCartItemPersistenceEntity.setQuantity(shoppingCartItem.quantity().value());
        shoppingCartItemPersistenceEntity.setPrice(shoppingCartItem.price().value());
        shoppingCartItemPersistenceEntity.setTotalAmount(shoppingCartItem.totalAmount().value());
        shoppingCartItemPersistenceEntity.setAvailable(shoppingCartItem.available());

        return shoppingCartItemPersistenceEntity;
    }
}
