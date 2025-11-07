package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.exception.ShoppingCartItemIncompatibleProductException;
import com.jonasesteves.algashop.ordering.domain.valueobject.Money;
import com.jonasesteves.algashop.ordering.domain.valueobject.Product;
import com.jonasesteves.algashop.ordering.domain.valueobject.ProductName;
import com.jonasesteves.algashop.ordering.domain.valueobject.Quantity;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ProductId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;
import lombok.Builder;

import java.util.Objects;

public class ShoppingCartItem {
    private ShoppingCartItemId id;
    private ShoppingCartId shoppingCartId;
    private ProductId productId;
    private ProductName name;
    private Quantity quantity;
    private Money price;
    private Money totalAmount;
    private Boolean available;

    @Builder(builderClassName = "ExistingShoppingCartItemBuilder", builderMethodName = "existing")
    private ShoppingCartItem(ShoppingCartItemId id, ShoppingCartId shoppingCartId, ProductId productId, ProductName productName,
                             Quantity quantity, Money price, Money totalAmount, Boolean available) {
        this.setId(id);
        this.setShoppingCartId(shoppingCartId);
        this.setProductId(productId);
        this.setName(productName);
        this.setQuantity(quantity);
        this.setPrice(price);
        this.setTotalAmount(totalAmount);
        this.setAvailable(available);
    }

    @Builder(builderClassName = "BrandNewShoppingCartItemBuilder", builderMethodName = "brandNew")
    private static ShoppingCartItem createBrandNew(ShoppingCartId shoppingCartId, ProductId productId, ProductName productName,
                                                   Quantity quantity, Money price, Boolean available) {
        Objects.requireNonNull(shoppingCartId);
        Objects.requireNonNull(productId);
        Objects.requireNonNull(productName);
        Objects.requireNonNull(quantity);
        Objects.requireNonNull(price);

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem(
                new ShoppingCartItemId(),
                shoppingCartId,
                productId,
                productName,
                quantity,
                price,
                Money.ZERO,
                available
        );

        shoppingCartItem.recalculateTotals();

        return shoppingCartItem;
    }

    public void changeQuantity(Quantity quantity) {
        Objects.requireNonNull(quantity);

        if (quantity.value() <= 0) {
            throw new IllegalArgumentException();
        }

        this.setQuantity(quantity);
        this.recalculateTotals();
    }

    public void refresh(Product product) {
        Objects.requireNonNull(product);

        if (!product.id().equals(this.productId())) {
            throw new ShoppingCartItemIncompatibleProductException(this.productId());
        }

        this.setName(product.name());
        this.setPrice(product.price());
        this.setAvailable(product.inStock());
        this.recalculateTotals();
    }

    public void recalculateTotals() {
        this.setTotalAmount(this.price().multiply(this.quantity()));
    }

    public ShoppingCartItemId id() {
        return id;
    }

    public ShoppingCartId shoppingCartId() {
        return shoppingCartId;
    }

    public ProductId productId() {
        return productId;
    }

    public ProductName name() {
        return name;
    }

    public Quantity quantity() {
        return quantity;
    }

    public Money price() {
        return price;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Boolean available() {
        return available;
    }

    private void setId(ShoppingCartItemId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setShoppingCartId(ShoppingCartId shoppingCartId) {
        Objects.requireNonNull(shoppingCartId);
        this.shoppingCartId = shoppingCartId;
    }

    private void setProductId(ProductId productId) {
        Objects.requireNonNull(productId);
        this.productId = productId;
    }

    private void setName(ProductName name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    private void setQuantity(Quantity quantity) {
        Objects.requireNonNull(quantity);
        this.quantity = quantity;
    }

    private void setPrice(Money price) {
        Objects.requireNonNull(price);
        this.price = price;
    }

    private void setTotalAmount(Money totalAmount) {
        Objects.requireNonNull(totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setAvailable(Boolean available) {
        Objects.requireNonNull(available);
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartItem that = (ShoppingCartItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
