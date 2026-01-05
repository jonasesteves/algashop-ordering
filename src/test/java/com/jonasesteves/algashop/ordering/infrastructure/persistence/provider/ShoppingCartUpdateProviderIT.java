package com.jonasesteves.algashop.ordering.infrastructure.persistence.provider;

import com.jonasesteves.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.entity.ProductTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.jonasesteves.algashop.ordering.domain.model.entity.ShoppingCartTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceEntityDisassembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({
        ShoppingCartUpdateProvider.class,
        ShoppingCartsPersistenceProvider.class,
        ShoppingCartPersistenceEntityAssembler.class,
        ShoppingCartPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ShoppingCartUpdateProviderIT {

    private final ShoppingCartsPersistenceProvider shoppingCartsPersistenceProvider;
    private final CustomersPersistenceProvider customersPersistenceProvider;
    private final ShoppingCartPersistenceEntityRepository repository;
    private final ShoppingCartUpdateProvider shoppingCartUpdateProvider;

    @Autowired
    ShoppingCartUpdateProviderIT(ShoppingCartsPersistenceProvider shoppingCartsPersistenceProvider, CustomersPersistenceProvider customersPersistenceProvider, ShoppingCartPersistenceEntityRepository repository, ShoppingCartUpdateProvider shoppingCartUpdateProvider) {
        this.shoppingCartsPersistenceProvider = shoppingCartsPersistenceProvider;
        this.customersPersistenceProvider = customersPersistenceProvider;
        this.repository = repository;
        this.shoppingCartUpdateProvider = shoppingCartUpdateProvider;
    }

    @BeforeEach
    void setup() {
        if (!customersPersistenceProvider.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customersPersistenceProvider.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldUpdateItemPriceAndTotalAmount() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someEmptyShoppingCart();

        Product product1 = ProductTestDataBuilder.someProduct().price(new Money("10000.00")).build();
        Product product2 = ProductTestDataBuilder.chocolate().price(new Money("10.00")).build();

        shoppingCart.addItem(product1, new Quantity(2));
        shoppingCart.addItem(product2, new Quantity(1));

        shoppingCartsPersistenceProvider.add(shoppingCart);

        ProductId productIdToUpdate = product1.id();
        Money newProductPrice = new Money("11000.00");
        Money expectedNewItemTotalPrice = newProductPrice.multiply(new Quantity(2));
        Money expectedNewCartTotalAmount = expectedNewItemTotalPrice.add(new Money("10.00"));

        shoppingCartUpdateProvider.adjustPrice(productIdToUpdate, newProductPrice);

        ShoppingCart updatedShoppingCart = shoppingCartsPersistenceProvider.ofId(shoppingCart.id()).orElseThrow();

        Assertions.assertThat(updatedShoppingCart.totalAmount()).isEqualTo(expectedNewCartTotalAmount);
        Assertions.assertThat(updatedShoppingCart.totalItems()).isEqualTo(new Quantity(3));

        ShoppingCartItem item = updatedShoppingCart.findItem(productIdToUpdate);

        Assertions.assertThat(item.totalAmount()).isEqualTo(expectedNewItemTotalPrice);
        Assertions.assertThat(item.price()).isEqualTo(newProductPrice);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldUpdateItemAvailability() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.someEmptyShoppingCart();

        Product product1 = ProductTestDataBuilder.someProduct().price(new Money("10000.00")).inStock(true).build();
        Product product2 = ProductTestDataBuilder.chocolate().price(new Money("10.00")).inStock(true).build();

        shoppingCart.addItem(product1, new Quantity(2));
        shoppingCart.addItem(product2, new Quantity(1));

        shoppingCartsPersistenceProvider.add(shoppingCart);

        ProductId productIdToUpdate = product1.id();
        ProductId productIdToNotUpdate = product2.id();
        shoppingCartUpdateProvider.changeAvailability(productIdToUpdate, false);

        ShoppingCart updatedShoppingCart = shoppingCartsPersistenceProvider.ofId(shoppingCart.id()).orElseThrow();

        ShoppingCartItem item1 = updatedShoppingCart.findItem(productIdToUpdate);
        ShoppingCartItem item2 = updatedShoppingCart.findItem(productIdToNotUpdate);

        Assertions.assertThat(item1.available()).isFalse();
        Assertions.assertThat(item2.available()).isTrue();
    }


}