package com.jonasesteves.algashop.ordering.infrastructure.client.rapidex;

import java.util.Objects;

public class DeliveryCostRequest {
    private String originZipCode;
    private String destinationZipCode;

    public DeliveryCostRequest() {
    }

    public DeliveryCostRequest(String originZipCode, String destinationZipCode) {
        this.originZipCode = originZipCode;
        this.destinationZipCode = destinationZipCode;
    }

    public String getOriginZipCode() {
        return originZipCode;
    }

    public void setOriginZipCode(String originZipCode) {
        this.originZipCode = originZipCode;
    }

    public String getDestinationZipCode() {
        return destinationZipCode;
    }

    public void setDestinationZipCode(String destinationZipCode) {
        this.destinationZipCode = destinationZipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryCostRequest that = (DeliveryCostRequest) o;
        return Objects.equals(originZipCode, that.originZipCode) && Objects.equals(destinationZipCode, that.destinationZipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originZipCode, destinationZipCode);
    }
}
