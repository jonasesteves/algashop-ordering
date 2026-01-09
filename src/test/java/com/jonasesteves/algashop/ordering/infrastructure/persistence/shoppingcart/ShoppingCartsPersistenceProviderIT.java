package com.jonasesteves.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.jonasesteves.algashop.ordering.domain.model.customer.Customer;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.SpringDataAuditingConfig;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityDisassembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.customer.CustomersPersistenceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

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
    void shouldFindShoppingCartByCustomerId() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();
        shoppingCartsPersistenceProvider.add(shoppingCart);

        ShoppingCart foundCart = shoppingCartsPersistenceProvider.ofCustomer(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID).orElseThrow();

        assertThat(foundCart).isNotNull();
        assertThat(foundCart.customerId()).isEqualTo(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID);
        assertThat(foundCart.id()).isEqualTo(shoppingCart.id());
    }

    @Test
    void shouldCorrectlyCountShoppingCarts() {
        long initialCount = shoppingCartsPersistenceProvider.count();

        ShoppingCart cart1 = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();
        shoppingCartsPersistenceProvider.add(cart1);

        Customer otherCustomer = CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).build();
        customersPersistenceProvider.add(otherCustomer);

        ShoppingCart cart2 = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();
        shoppingCartsPersistenceProvider.add(cart2);

        long finalCount = shoppingCartsPersistenceProvider.count();

        assertThat(finalCount).isEqualTo(initialCount + 2);
    }

    @Test
    void shouldRemoveShoppingCartById() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();
        shoppingCartsPersistenceProvider.add(shoppingCart);
        assertThat(shoppingCartsPersistenceProvider.exists(shoppingCart.id())).isTrue();

        shoppingCartsPersistenceProvider.remove(shoppingCart.id());

        assertThat(shoppingCartsPersistenceProvider.exists(shoppingCart.id())).isFalse();
        assertThat(repository.findById(shoppingCart.id().value())).isEmpty();
    }

    @Test
    void shouldRemoveShoppingCartByEntity() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someShoppingCartWithProduct();
        shoppingCartsPersistenceProvider.add(shoppingCart);
        assertThat(shoppingCartsPersistenceProvider.exists(shoppingCart.id())).isTrue();

        shoppingCartsPersistenceProvider.remove(shoppingCart);

        assertThat(shoppingCartsPersistenceProvider.exists(shoppingCart.id())).isFalse();
    }

    @Test
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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
}