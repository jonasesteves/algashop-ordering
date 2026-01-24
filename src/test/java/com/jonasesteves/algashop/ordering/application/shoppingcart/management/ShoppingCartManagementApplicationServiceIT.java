package com.jonasesteves.algashop.ordering.application.shoppingcart.management;

import com.jonasesteves.algashop.ordering.domain.model.commons.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customer;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customers;
import com.jonasesteves.algashop.ordering.domain.model.product.Product;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductCatalogService;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductOutOfStockException;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartDoesNotContainItemException;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
class ShoppingCartManagementApplicationServiceIT {

    @Autowired
    private ShoppingCartManagementApplicationService shoppingCartManagementApplicationService;

    @Autowired
    private ShoppingCarts shoppingCarts;

    @Autowired
    private Customers customers;

    @MockitoBean
    private ProductCatalogService productCatalogService;

    @Test
    void shouldCreateNewShoppingCart() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);

        UUID shoppingCartId = shoppingCartManagementApplicationService.createNew(customer.id().value());

        Assertions.assertThat(shoppingCartId).isNotNull();

        Optional<ShoppingCart> createdCart = shoppingCarts.ofId(new ShoppingCartId(shoppingCartId));
        Assertions.assertThat(createdCart).isPresent();
        Assertions.assertThat(createdCart.get().customerId().value()).isEqualTo(customer.id().value());
        Assertions.assertThat(createdCart.get().isEmpty()).isTrue();
    }

    @Test
    void shouldAddItemToCart() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        Product product = ProductTestDataBuilder.someProduct().inStock(true).build();
        Mockito.when(productCatalogService.ofId(product.id())).thenReturn(Optional.of(product));

        ShoppingCartItemInput shoppingCartITemInput = ShoppingCartItemInput.builder()
                .shoppingCartId(shoppingCart.id().value())
                .productId(product.id().value())
                .quantity(2)
                .build();

        shoppingCartManagementApplicationService.addItem(shoppingCartITemInput);
        ShoppingCart updatedCart = shoppingCarts.ofId(shoppingCart.id()).orElseThrow();
        Assertions.assertThat(updatedCart.items()).hasSize(1);
        Assertions.assertThat(updatedCart.items().iterator().next().productId()).isEqualTo(product.id());
        Assertions.assertThat(updatedCart.items().iterator().next().quantity().value()).isEqualTo(2);
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenCreatingNewShoppingCartForNonExistingCustomer() {
        UUID nonExistingCustomerId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.createNew(nonExistingCustomerId));
    }

    @Test
    void shouldThrowCustomerAlreadyHaveShoppingCartExceptionWhenCreatingNewShoppingCartForCustomerWithExistingCart() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);
        ShoppingCart existingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(existingCart);

        UUID customerId = customer.id().value();

        Assertions.assertThatExceptionOfType(CustomerAlreadyHaveShoppingCartException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.createNew(customerId));
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenAddingItemToNonExistingShoppingCart() {
        UUID nonExistingCartId = UUID.randomUUID();
        Product product = ProductTestDataBuilder.someProduct().inStock(true).build();
        Mockito.when(productCatalogService.ofId(product.id())).thenReturn(Optional.of(product));

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(nonExistingCartId)
                .productId(product.id().value())
                .quantity(1)
                .build();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.addItem(input));
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenAddingNonExistingProduct() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        Mockito.when(productCatalogService.ofId(Mockito.any())).thenReturn(Optional.empty());

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(shoppingCart.id().value())
                .productId(UUID.randomUUID())
                .quantity(1)
                .build();

        Assertions.assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.addItem(input));
    }

    @Test
    void shouldThrowProductOutOfStockExceptionWhenAddingOutOfStockProduct() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        Product outOfStockProduct = ProductTestDataBuilder.someProduct().inStock(false).build();
        Mockito.when(productCatalogService.ofId(outOfStockProduct.id())).thenReturn(Optional.of(outOfStockProduct));

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(shoppingCart.id().value())
                .productId(outOfStockProduct.id().value())
                .quantity(1)
                .build();

        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.addItem(input));
    }

    @Test
    void shouldRemoveItemFromShoppingCartSuccessfully() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        Product product = ProductTestDataBuilder.someProduct().inStock(true).build();
        shoppingCart.addItem(product, new Quantity(1));
        shoppingCarts.add(shoppingCart);

        ShoppingCartItem itemToRemove = shoppingCart.items().iterator().next();

        shoppingCartManagementApplicationService.removeItem(shoppingCart.id().value(), itemToRemove.id().value());

        ShoppingCart updatedCart = shoppingCarts.ofId(shoppingCart.id()).orElseThrow();
        Assertions.assertThat(updatedCart.items()).isEmpty();
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenRemovingItemFromNonExistingShoppingCart() {
        UUID nonExistingCartId = UUID.randomUUID();
        UUID dummyItemId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.removeItem(nonExistingCartId, dummyItemId));
    }

    @Test
    void shouldThrowShoppingCartDoesNotContainItemExceptionWhenRemovingNonExistingItem() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        UUID nonExistingItemId = UUID.randomUUID();
        UUID shoppingCartId = shoppingCart.id().value();

        Assertions.assertThatExceptionOfType(ShoppingCartDoesNotContainItemException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.removeItem(shoppingCartId, nonExistingItemId));
    }

    @Test
    void shouldEmptyShoppingCartSuccessfully() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCart.addItem(ProductTestDataBuilder.someProduct().inStock(true).build(), new Quantity(1));
        shoppingCarts.add(shoppingCart);

        shoppingCartManagementApplicationService.empty(shoppingCart.id().value());

        ShoppingCart updatedCart = shoppingCarts.ofId(shoppingCart.id()).orElseThrow();
        Assertions.assertThat(updatedCart.isEmpty()).isTrue();
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenEmptyingNonExistingShoppingCart() {
        UUID nonExistingCartId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.empty(nonExistingCartId));
    }

    @Test
    void shouldDeleteShoppingCartSuccessfully() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        shoppingCartManagementApplicationService.delete(shoppingCart.id().value());

        Optional<ShoppingCart> deletedCart = shoppingCarts.ofId(shoppingCart.id());
        Assertions.assertThat(deletedCart).isNotPresent();
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenDeletingNonExistingShoppingCart() {
        UUID nonExistingCartId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.delete(nonExistingCartId));
    }
}