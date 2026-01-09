package com.jonasesteves.algashop.ordering.infrastructure.shipping.client.rapidex;

import java.util.Objects;

public class DeliveryCostResponse {
    private String deliveryCost;
    private Long estimatedDaysToDelivery;

    public DeliveryCostResponse() {
    }

    public DeliveryCostResponse(String deliveryCost, Long estimatedDaysToDelivery) {
        this.deliveryCost = deliveryCost;
        this.estimatedDaysToDelivery = estimatedDaysToDelivery;
    }

    public String getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(String deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public Long getEstimatedDaysToDelivery() {
        return estimatedDaysToDelivery;
    }

    public void setEstimatedDaysToDelivery(Long estimatedDaysToDelivery) {
        this.estimatedDaysToDelivery = estimatedDaysToDelivery;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryCostResponse that = (DeliveryCostResponse) o;
        return Objects.equals(deliveryCost, that.deliveryCost) && Objects.equals(estimatedDaysToDelivery, that.estimatedDaysToDelivery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryCost, estimatedDaysToDelivery);
    }
}
