package com.jonasesteves.algashop.ordering.infrastructure.persistence.provider;

import com.jonasesteves.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.entity.ProductTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCartItemTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCartTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceEntityDisassembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.ShoppingCartItemPersistenceEntity;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntityTestDataBuilder;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
@Import({
        ShoppingCartsPersistenceProvider.class,
        ShoppingCartPersistenceEntityAssembler.class,
        ShoppingCartPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class ShoppingCartsPersistenceProviderIT {

    private final ShoppingCartsPersistenceProvider shoppingCartsPersistenceProvider;
    private final CustomersPersistenceProvider customersPersistenceProvider;
    private final ShoppingCartPersistenceEntityRepository repository;

    @Autowired
    ShoppingCartsPersistenceProviderIT(ShoppingCartsPersistenceProvider shoppingCartsPersistenceProvider, CustomersPersistenceProvider customersPersistenceProvider, ShoppingCartPersistenceEntityRepository repository) {
        this.shoppingCartsPersistenceProvider = shoppingCartsPersistenceProvider;
        this.customersPersistenceProvider = customersPersistenceProvider;
        this.repository = repository;
    }

    @BeforeEach
    void setup() {
        if (!customersPersistenceProvider.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customersPersistenceProvider.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldAddAndFindAndNotFailWhenNoTransaction() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();

        assertThat(shoppingCart.version()).isNull();

        shoppingCartsPersistenceProvider.add(shoppingCart);

        assertThat(shoppingCart.version()).isNotNull().isEqualTo(0L);

        assertThatNoException().isThrownBy(() -> {
            ShoppingCart found = shoppingCartsPersistenceProvider.ofId(shoppingCart.id()).orElseThrow();
            assertThat(found).isNotNull();
            assertThat(found.id()).isEqualTo(shoppingCart.id());
            assertThat(found.totalItems().value()).isEqualTo(2);
            assertThat(found.items()).hasSize(1);
        });
    }

    @Test
    void shouldPersistWithAuditData() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();
        UUID shoppingCartId = shoppingCart.id().value();

        shoppingCartsPersistenceProvider.add(shoppingCart);

        ShoppingCartPersistenceEntity shoppingCartPersistenceEntity = repository.findById(shoppingCartId).orElseThrow();

        assertThat(shoppingCartPersistenceEntity.getCreatedByUserId()).isNotNull();
        assertThat(shoppingCartPersistenceEntity.getCreatedAt()).isNotNull();
        assertThat(shoppingCartPersistenceEntity.getLastModifiedByUserId()).isNotNull();
        assertThat(shoppingCartPersistenceEntity.getLastModifiedAt()).isNotNull();
    }
}