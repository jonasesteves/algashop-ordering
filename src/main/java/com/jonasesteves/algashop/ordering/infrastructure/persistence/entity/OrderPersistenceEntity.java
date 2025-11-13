package com.jonasesteves.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "\"order\"")
@Builder
public class OrderPersistenceEntity {

    @Id
    private Long id; //TSID

    private UUID customerId;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private String status;
    private String paymentMethod;
    private OffsetDateTime placedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime readyAt;

    public OrderPersistenceEntity() {
    }

    public OrderPersistenceEntity(Long id, UUID customerId, BigDecimal totalAmount, Integer totalItems, String status,
                                  String paymentMethod, OffsetDateTime placedAt, OffsetDateTime paidAt,
                                  OffsetDateTime canceledAt, OffsetDateTime readyAt) {
        this.id = id;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.placedAt = placedAt;
        this.paidAt = paidAt;
        this.canceledAt = canceledAt;
        this.readyAt = readyAt;
    }

    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID customerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public BigDecimal totalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer totalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public String status() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String paymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OffsetDateTime placedAt() {
        return placedAt;
    }

    public void setPlacedAt(OffsetDateTime placedAt) {
        this.placedAt = placedAt;
    }

    public OffsetDateTime paidAt() {
        return paidAt;
    }

    public void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public OffsetDateTime canceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(OffsetDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public OffsetDateTime readyAt() {
        return readyAt;
    }

    public void setReadyAt(OffsetDateTime readyAt) {
        this.readyAt = readyAt;
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
}
