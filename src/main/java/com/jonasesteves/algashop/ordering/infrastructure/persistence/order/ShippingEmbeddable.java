package com.jonasesteves.algashop.ordering.infrastructure.persistence.order;

import com.jonasesteves.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class ShippingEmbeddable {

    private BigDecimal cost;

    private LocalDate expectedDate;

    @Embedded
    private RecipientEmbeddable recipient;

    @Embedded
    private AddressEmbeddable address;

    public ShippingEmbeddable() {
    }

    public ShippingEmbeddable(BigDecimal cost, LocalDate expectedDate, RecipientEmbeddable recipient, AddressEmbeddable address) {
        this.cost = cost;
        this.expectedDate = expectedDate;
        this.recipient = recipient;
        this.address = address;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDate getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(LocalDate expectedDate) {
        this.expectedDate = expectedDate;
    }

    public RecipientEmbeddable getRecipient() {
        return recipient;
    }

    public void setRecipient(RecipientEmbeddable recipient) {
        this.recipient = recipient;
    }

    public AddressEmbeddable getAddress() {
        return address;
    }

    public void setAddress(AddressEmbeddable address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShippingEmbeddable that = (ShippingEmbeddable) o;
        return Objects.equals(cost, that.cost) && Objects.equals(expectedDate, that.expectedDate) && Objects.equals(recipient, that.recipient) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost, expectedDate, recipient, address);
    }

    @Override
    public String toString() {
        return "ShippingEmbeddable{" +
                "cost=" + cost +
                ", expectedDate=" + expectedDate +
                ", recipient=" + recipient +
                ", address=" + address +
                '}';
    }
}
