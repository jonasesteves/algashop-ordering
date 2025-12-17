package com.jonasesteves.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "shopping_cart_item")
@EntityListeners(AuditingEntityListener.class)
public class ShoppingCartItemPersistenceEntity {

    @Id
    private UUID id;

    @JoinColumn
    @ManyToOne(optional = false)
    private ShoppingCartPersistenceEntity shoppingCart;

    private UUID productId;
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalAmount;
    private Boolean available;

    @CreatedBy
    private UUID createdByUserId;

    @CreatedDate
    private OffsetDateTime createdAt;

    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    @Version
    private Long version;

    public ShoppingCartItemPersistenceEntity() {
    }

    public ShoppingCartItemPersistenceEntity(UUID id, ShoppingCartPersistenceEntity shoppingCart, UUID productId,
                                             String name, Integer quantity, BigDecimal price, BigDecimal totalAmount,
                                             Boolean available, UUID createdByUserId, OffsetDateTime createdAt,
                                             UUID lastModifiedByUserId, OffsetDateTime lastModifiedAt, Long version) {
        this.id = id;
        this.shoppingCart = shoppingCart;
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
        this.available = available;
        this.createdByUserId = createdByUserId;
        this.createdAt = createdAt;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.lastModifiedAt = lastModifiedAt;
        this.version = version;
    }

    private ShoppingCartPersistenceEntity getShoppingCart() {
        return shoppingCart;
    }

    public UUID getShoppingCartId() {
        if (getShoppingCart() == null) {
            return null;
        }
        return getShoppingCart().getId();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setShoppingCart(ShoppingCartPersistenceEntity shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public UUID getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(UUID createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(UUID lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public OffsetDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(OffsetDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartItemPersistenceEntity that = (ShoppingCartItemPersistenceEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ShoppingCartItemPersistenceEntity{" +
                "id=" + id +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private ShoppingCartPersistenceEntity shoppingCart;
        private UUID productId;
        private String name;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal totalAmount;
        private Boolean available;
        private UUID createdByUserId;
        private OffsetDateTime createdAt;
        private UUID lastModifiedByUserId;
        private OffsetDateTime lastModifiedAt;
        private Long version;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder shoppingCart(ShoppingCartPersistenceEntity shoppingCart) {
            this.shoppingCart = shoppingCart;
            return this;
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder available(Boolean available) {
            this.available = available;
            return this;
        }

        public Builder createdByUserId(UUID createdByUserId) {
            this.createdByUserId = createdByUserId;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastModifiedByUserId(UUID lastModifiedByUserId) {
            this.lastModifiedByUserId = lastModifiedByUserId;
            return this;
        }

        public Builder lastModifiedAt(OffsetDateTime lastModifiedAt) {
            this.lastModifiedAt = lastModifiedAt;
            return this;
        }

        public Builder version(Long version) {
            this.version = version;
            return this;
        }

        public ShoppingCartItemPersistenceEntity build() {
            return new ShoppingCartItemPersistenceEntity(id, shoppingCart, productId, name, quantity, price,
                    totalAmount, available, createdByUserId, createdAt, lastModifiedByUserId, lastModifiedAt, version);
        }

        @Override
        public String toString() {
            return "ShoppingCartItemPersistenceEntity.Builder{" +
                    "id=" + id +
                    '}';
        }
    }
}
