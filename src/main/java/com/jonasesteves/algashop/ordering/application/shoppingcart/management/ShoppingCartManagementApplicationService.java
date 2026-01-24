package com.jonasesteves.algashop.ordering.application.shoppingcart.management;

import com.jonasesteves.algashop.ordering.domain.model.commons.Quantity;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.product.Product;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductCatalogService;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductId;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemId;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
public class ShoppingCartManagementApplicationService {

    private final ShoppingCarts shoppingCarts;
    private final ProductCatalogService productCatalogService;
    private final ShoppingService shoppingService;

    public ShoppingCartManagementApplicationService(ShoppingCarts shoppingCarts, ProductCatalogService productCatalogService, ShoppingService shoppingService) {
        this.shoppingCarts = shoppingCarts;
        this.productCatalogService = productCatalogService;
        this.shoppingService = shoppingService;
    }

    @Transactional
    public void addItem(ShoppingCartItemInput input) {
        Objects.requireNonNull(input);

        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(input.getShoppingCartId()))
                .orElseThrow(ShoppingCartNotFoundException::new);

        Product product = productCatalogService.ofId(new ProductId(input.getProductId()))
                .orElseThrow(ProductNotFoundException::new);

        shoppingCart.addItem(product, new Quantity(input.getQuantity()));
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public UUID createNew(UUID rawCustomerId) {
        Objects.requireNonNull(rawCustomerId);
        ShoppingCart shoppingCart = shoppingService.startShopping(new CustomerId(rawCustomerId));
        shoppingCarts.add(shoppingCart);
        return shoppingCart.id().value();
    }

    @Transactional
    public void removeItem(UUID rawShoppingCartId, UUID rawShoppingCartItemId) {
        Objects.requireNonNull(rawShoppingCartId);
        Objects.requireNonNull(rawShoppingCartItemId);

        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
                .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCart.removeItem(new ShoppingCartItemId(rawShoppingCartItemId));
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public void empty(UUID rawShoppingCartId) {
        Objects.requireNonNull(rawShoppingCartId);

        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
                .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCart.empty();
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public void delete(UUID rawShoppingCartId) {
        Objects.requireNonNull(rawShoppingCartId);

        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
                .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCarts.remove(shoppingCart);
    }
}
