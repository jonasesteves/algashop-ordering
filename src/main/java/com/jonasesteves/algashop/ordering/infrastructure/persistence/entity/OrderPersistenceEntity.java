package com.jonasesteves.algashop.ordering.infrastructure.persistence.entity;

import com.jonasesteves.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Builder;
import org.springframework.data.annotation.CreatedBy;
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
@Table(name = "\"order\"")
@EntityListeners(AuditingEntityListener.class)
public class OrderPersistenceEntity {

    @Id
    private Long id; //TSID

    @JoinColumn
    @ManyToOne(optional = false)
    private CustomerPersistenceEntity customer;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private String status;
    private String paymentMethod;
    private OffsetDateTime placedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime readyAt;

    @CreatedBy
    private UUID createdByUserId;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @Version
    private Long version;

    @AttributeOverride(name = "firstName", column = @Column(name = "billing_first_name"))
    @AttributeOverride(name = "lastName", column = @Column(name = "billing_last_name"))
    @AttributeOverride(name = "document", column = @Column(name = "billing_document"))
    @AttributeOverride(name = "phone", column = @Column(name = "billing_phone"))
    @AttributeOverride(name = "address.street", column = @Column(name = "billing_address_street"))
    @AttributeOverride(name = "address.number", column = @Column(name = "billing_address_number"))
    @AttributeOverride(name = "address.complement", column = @Column(name = "billing_address_complement"))
    @AttributeOverride(name = "address.neighborhood", column = @Column(name = "billing_address_neighborhood"))
    @AttributeOverride(name = "address.city", column = @Column(name = "billing_address_city"))
    @AttributeOverride(name = "address.state", column = @Column(name = "billing_address_state"))
    @AttributeOverride(name = "address.zipCode", column = @Column(name = "billing_address_zipCode"))
    @Embedded
    private BillingEmbeddable billing;

    @AttributeOverride(name = "cost", column = @Column(name = "shipping_cost"))
    @AttributeOverride(name = "expectedDate", column = @Column(name = "shipping_expected_date"))
    @AttributeOverride(name = "recipient.firstName", column = @Column(name = "shipping_recipient_first_name"))
    @AttributeOverride(name = "recipient.lastName", column = @Column(name = "shipping_recipient_last_name"))
    @AttributeOverride(name = "recipient.document", column = @Column(name = "shipping_recipient_document"))
    @AttributeOverride(name = "recipient.phone", column = @Column(name = "shipping_recipient_phone"))
    @AttributeOverride(name = "address.street", column = @Column(name = "shipping_address_street"))
    @AttributeOverride(name = "address.number", column = @Column(name = "shipping_address_number"))
    @AttributeOverride(name = "address.complement", column = @Column(name = "shipping_address_complement"))
    @AttributeOverride(name = "address.neighborhood", column = @Column(name = "shipping_address_neighborhood"))
    @AttributeOverride(name = "address.city", column = @Column(name = "shipping_address_city"))
    @AttributeOverride(name = "address.state", column = @Column(name = "shipping_address_state"))
    @AttributeOverride(name = "address.zipCode", column = @Column(name = "shipping_address_zipCode"))
    @Embedded
    private ShippingEmbeddable shipping;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItemPersistenceEntity> items = new HashSet<>();


    public OrderPersistenceEntity() {
    }

    @Builder
    public OrderPersistenceEntity(Long id, CustomerPersistenceEntity customer, BigDecimal totalAmount, Integer totalItems, String status,
                                  String paymentMethod, OffsetDateTime placedAt, OffsetDateTime paidAt,
                                  OffsetDateTime canceledAt, OffsetDateTime readyAt, UUID createdByUserId,
                                  OffsetDateTime lastModifiedAt, UUID lastModifiedByUserId, Long version,
                                  BillingEmbeddable billing, ShippingEmbeddable shipping, Set<OrderItemPersistenceEntity> items) {
        this.id = id;
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.placedAt = placedAt;
        this.paidAt = paidAt;
        this.canceledAt = canceledAt;
        this.readyAt = readyAt;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAt = lastModifiedAt;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
        this.billing = billing;
        this.shipping = shipping;
        this.replaceItems(items);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OffsetDateTime getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(OffsetDateTime placedAt) {
        this.placedAt = placedAt;
    }

    public OffsetDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public OffsetDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(OffsetDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public OffsetDateTime getReadyAt() {
        return readyAt;
    }

    public void setReadyAt(OffsetDateTime readyAt) {
        this.readyAt = readyAt;
    }

    public BillingEmbeddable getBilling() {
        return billing;
    }

    public void setBilling(BillingEmbeddable billing) {
        this.billing = billing;
    }

    public ShippingEmbeddable getShipping() {
        return shipping;
    }

    public void setShipping(ShippingEmbeddable shipping) {
        this.shipping = shipping;
    }

    public UUID getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(UUID createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public OffsetDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(OffsetDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public UUID getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(UUID lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Set<OrderItemPersistenceEntity> getItems() {
        return items;
    }

    public void setItems(Set<OrderItemPersistenceEntity> items) {
        this.items = items;
    }

    public void replaceItems(Set<OrderItemPersistenceEntity> items) {
        if (items == null || items.isEmpty()) {
            this.setItems(new HashSet<>());
            return;
        }

        items.forEach(i -> i.setOrder(this));
        this.setItems(items);
    }

    public void addItem(OrderItemPersistenceEntity item) {
        if (item == null) return;

        if (this.getItems() == null) {
            this.setItems(new HashSet<>());
        }

        item.setOrder(this);
        this.getItems().add(item);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderPersistenceEntity that = (OrderPersistenceEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderPersistenceEntity{" +
                "id=" + id +
                '}';
    }

    public UUID getCustomerId() {
        if (this.customer == null) return null;
        return this.customer.getId();
    }
}
