package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.exception.ShoppingCartDoesNotContainItemException;
import com.jonasesteves.algashop.ordering.domain.exception.ShoppingCartDoesNotContainProductException;
import com.jonasesteves.algashop.ordering.domain.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ProductId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

public class ShoppingCart {

    private ShoppingCartId id;
    private CustomerId customerId;
    private Money totalAmount;
    private Quantity totalItems;
    private Set<ShoppingCartItem> items;
    private OffsetDateTime createdAt;

    private ShoppingCart(ShoppingCartId id, CustomerId customerId, Money totalAmount, Quantity totalItems, OffsetDateTime createdAt, Set<ShoppingCartItem> items) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setTotalAmount(totalAmount);
        this.setTotalItems(totalItems);
        this.setCreatedAt(createdAt);
        this.setItems(items);
    }

    public static ShoppingCart startShopping(CustomerId customerId) {
        return new ShoppingCart(
                new ShoppingCartId(),
                customerId,
                Money.ZERO,
                Quantity.ZERO,
                OffsetDateTime.now(),
                new HashSet<>()
        );
    }

    public void addItem(Product product, Quantity quantity) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(quantity);

        product.checkOutOfStock();

        ShoppingCartItem shoppingCartItem = ShoppingCartItem.brandNew()
                .shoppingCartId(this.id())
                .productId(product.id())
                .productName(product.name())
                .price(product.price())
                .quantity(quantity)
                .available(product.inStock())
                .build();

        searchItemByProductId(product.id()).ifPresentOrElse(
                i -> updateItem(i, product, quantity),
                () -> insertItem(shoppingCartItem)
        );

        this.recalculateTotals();
    }

    public void removeItem(ShoppingCartItemId shoppingCartItemId) {
        Objects.requireNonNull(shoppingCartItemId);

        ShoppingCartItem shoppingCartItem = findItem(shoppingCartItemId);

        this.items.remove(shoppingCartItem);
        this.recalculateTotals();
    }

    public void refreshItem(Product product) {
        Objects.requireNonNull(product);
        ShoppingCartItem shoppingCartItem = findItem(product.id());
        shoppingCartItem.refresh(product);
        this.recalculateTotals();
    }

    public void changeItemQuantity(ShoppingCartItemId shoppingCartItemId, Quantity quantity) {
        Objects.requireNonNull(shoppingCartItemId);
        Objects.requireNonNull(quantity);

        ShoppingCartItem shoppingCartItem = findItem(shoppingCartItemId);
        shoppingCartItem.changeQuantity(quantity);
        this.recalculateTotals();
    }

    public void empty() {
        this.items.clear();
        setTotalAmount(Money.ZERO);
        setTotalItems(Quantity.ZERO);
    }

    public boolean containsUnavailableItems() {
        return this.items().stream().anyMatch(i -> i.available().equals(false));
    }

    public void recalculateTotals() {
        BigDecimal totalCartAmount = this.items().stream()
                .map(s -> s.totalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer quantity = this.items().stream()
                .map(cartItem -> cartItem.quantity().value())
                .reduce(0, Integer::sum);

        this.setTotalItems(new Quantity(quantity));
        this.setTotalAmount(new Money(totalCartAmount));
    }

    public ShoppingCartItem findItem(ShoppingCartItemId shoppingCartItemId) {
        return this.items().stream()
                .filter(s -> s.id().equals(shoppingCartItemId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartDoesNotContainItemException(this.id(), shoppingCartItemId));
    }

    public ShoppingCartItem findItem(ProductId productId) {
        return this.items().stream()
                .filter(s -> s.productId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartDoesNotContainProductException(this.id(), productId));
    }

    public ShoppingCartId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    public Set<ShoppingCartItem> items() {
        return Collections.unmodifiableSet(this.items);
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    private void updateItem(ShoppingCartItem shoppingCartItem, Product product, Quantity quantity) {
        shoppingCartItem.refresh(product);
        shoppingCartItem.changeQuantity(shoppingCartItem.quantity().add(quantity));
    }

    private Optional<ShoppingCartItem> searchItemByProductId(ProductId productId) {
        Objects.requireNonNull(productId);
        return this.items().stream().filter(s -> s.productId().equals(productId)).findFirst();
    }

    private void insertItem(ShoppingCartItem shoppingCartItem) {
        this.items.add(shoppingCartItem);
    }

    private void setId(ShoppingCartId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setCustomerId(CustomerId customerId) {
        Objects.requireNonNull(customerId);
        this.customerId = customerId;
    }

    private void setTotalAmount(Money totalAmount) {
        Objects.requireNonNull(totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setTotalItems(Quantity totalItems) {
        Objects.requireNonNull(totalItems);
        this.totalItems = totalItems;
    }

    private void setItems(Set<ShoppingCartItem> items) {
        Objects.requireNonNull(items);
        this.items = items;
    }

    private void setCreatedAt(OffsetDateTime createdAt) {
        Objects.requireNonNull(createdAt);
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
