package com.jonasesteves.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.SpringDataAuditingConfig;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SpringDataAuditingConfig.class)
class ShoppingCartPersistenceEntityRepositoryIT {

    private final ShoppingCartPersistenceEntityRepository shoppingCartPersistenceEntityRepository;
    private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    @Autowired
    ShoppingCartPersistenceEntityRepositoryIT(ShoppingCartPersistenceEntityRepository shoppingCartPersistenceEntityRepository, CustomerPersistenceEntityRepository customerPersistenceEntityRepository) {
        this.shoppingCartPersistenceEntityRepository = shoppingCartPersistenceEntityRepository;
        this.customerPersistenceEntityRepository = customerPersistenceEntityRepository;
    }

    private CustomerPersistenceEntity customerPersistenceEntity;


    @BeforeEach
    void setup() {
        UUID customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID.value();
        if (!customerPersistenceEntityRepository.existsById(customerId)) {
            customerPersistenceEntity = customerPersistenceEntityRepository.saveAndFlush(
                    CustomerPersistenceEntityTestDataBuilder.someCustomer().build()
            );
        }
    }

    @Test
    void shouldPersist() {
        ShoppingCartPersistenceEntity entity = ShoppingCartPersistenceEntityTestDataBuilder.existingShoppingCart()
                .customer(customerPersistenceEntity)
                .build();

        shoppingCartPersistenceEntityRepository.saveAndFlush(entity);
        assertThat(shoppingCartPersistenceEntityRepository.existsById(entity.getId())).isTrue();

        ShoppingCartPersistenceEntity savedEntity = shoppingCartPersistenceEntityRepository.findById(entity.getId()).orElseThrow();

        assertThat(savedEntity.getItems()).isNotEmpty();
    }

    @Test
    void shouldCount() {
        long shoppingCartsCount = shoppingCartPersistenceEntityRepository.count();
        assertThat(shoppingCartsCount).isZero();
    }

    @Test
    void shouldPersistWithAuditData() {
        ShoppingCartPersistenceEntity entity = ShoppingCartPersistenceEntityTestDataBuilder.existingShoppingCart()
                .customer(customerPersistenceEntity)
                .build();

        shoppingCartPersistenceEntityRepository.saveAndFlush(entity);

        ShoppingCartPersistenceEntity shoppingCartPersistenceEntity = shoppingCartPersistenceEntityRepository.findById(entity.getId()).orElseThrow();

        assertThat(shoppingCartPersistenceEntity.getCreatedByUserId()).isNotNull();
        assertThat(shoppingCartPersistenceEntity.getCreatedAt()).isNotNull();
        assertThat(shoppingCartPersistenceEntity.getLastModifiedByUserId()).isNotNull();
        assertThat(shoppingCartPersistenceEntity.getLastModifiedAt()).isNotNull();
    }

//    @Test
//    void shouldUpdateAddingNewItem() {
//        ShoppingCartPersistenceEntity entity = ShoppingCartPersistenceEntityTestDataBuilder.existingShoppingCart()
//                .customer(customerPersistenceEntity)
//                .build();
//
//        entity = shoppingCartPersistenceEntityRepository.saveAndFlush(entity);
//
//
//        ShoppingCartItemPersistenceEntity item = ShoppingCartPersistenceEntityTestDataBuilder.existingItemAlt2().build();
//        entity.addItem(item);
//        entity = shoppingCartPersistenceEntityRepository.saveAndFlush(entity);
//
//        assertThat(entity.getItems()).hasSize(2);
//    }
}