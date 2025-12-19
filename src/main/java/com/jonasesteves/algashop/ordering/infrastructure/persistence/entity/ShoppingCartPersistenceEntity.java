package com.jonasesteves.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "\"shopping_cart\"")
@EntityListeners(AuditingEntityListener.class)
public class ShoppingCartPersistenceEntity {

    @Id
    private UUID id;

    @JoinColumn
    @ManyToOne(optional = false)
    private CustomerPersistenceEntity customer;

    private BigDecimal totalAmount;
    private Integer totalItems;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShoppingCartItemPersistenceEntity> items = new HashSet<>();

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

    public ShoppingCartPersistenceEntity() {
    }

    public ShoppingCartPersistenceEntity(UUID id, CustomerPersistenceEntity customer, BigDecimal totalAmount,
                                         Integer totalItems, Set<ShoppingCartItemPersistenceEntity> items,
                                         OffsetDateTime createdAt) {
        this.id = id;
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.replaceItems(items);
        this.createdAt = createdAt;
    }

    public void replaceItems(Set<ShoppingCartItemPersistenceEntity> items) {
        if (items == null || items.isEmpty()) {
            this.setItems(new HashSet<>());
            return;
        }
        items.forEach(i -> i.setShoppingCart(this));
        this.setItems(items);
    }

    public void addItem(Set<ShoppingCartItemPersistenceEntity> items) {
        for (ShoppingCartItemPersistenceEntity item : items) {
            this.addItem(item);
        }
    }

    public void addItem(ShoppingCartItemPersistenceEntity item) {
        if (item == null) return;

        if (this.getItems() == null) {
            this.setItems(new HashSet<>());
        }

        item.setShoppingCart(this);
        this.items.add(item);
    }

    public UUID getCustomerId() {
        if (customer == null) {
            return null;
        }
        return customer.getId();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CustomerPersistenceEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerPersistenceEntity customer) {
        this.customer = customer;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer quantity) {
        this.totalItems = quantity;
    }

    public Set<ShoppingCartItemPersistenceEntity> getItems() {
        return items;
    }

    public void setItems(Set<ShoppingCartItemPersistenceEntity> items) {
        this.items = items;
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
        ShoppingCartPersistenceEntity that = (ShoppingCartPersistenceEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ShoppingCartPersistenceEntity{" +
                "id=" + id +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private CustomerPersistenceEntity customer;
        private BigDecimal totalAmount;
        private Integer totalItems;
        private Set<ShoppingCartItemPersistenceEntity> items = new HashSet<>();
        private OffsetDateTime createdAt;


        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder customer(CustomerPersistenceEntity customer) {
            this.customer = customer;
            return this;
        }

        public Builder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder totalItems(Integer totalItems) {
            this.totalItems = totalItems;
            return this;
        }

        public Builder items(Set<ShoppingCartItemPersistenceEntity> items) {
            this.items = items;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ShoppingCartPersistenceEntity build() {
            return new ShoppingCartPersistenceEntity(
                    id,
                    customer,
                    totalAmount,
                    totalItems,
                    items,
                    createdAt
            );
        }

        @Override
        public String toString() {
            return "ShoppingCartPersistenceEntity.Builder{" +
                    "id=" + id +
                    '}';
        }
    }
}
