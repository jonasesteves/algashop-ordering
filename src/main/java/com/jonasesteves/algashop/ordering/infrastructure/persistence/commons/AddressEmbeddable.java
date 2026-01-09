package com.jonasesteves.algashop.ordering.infrastructure.persistence.commons;

import jakarta.persistence.Embeddable;
import lombok.Builder;

import java.util.Objects;

@Embeddable
public class AddressEmbeddable {
    private String number;
    private String street;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;

    public AddressEmbeddable() {
    }

    @Builder
    public AddressEmbeddable(String number, String street, String complement, String neighborhood, String city, String state, String zipCode) {
        this.number = number;
        this.street = street;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AddressEmbeddable that = (AddressEmbeddable) o;
        return Objects.equals(number, that.number) && Objects.equals(street, that.street) && Objects.equals(complement, that.complement) && Objects.equals(neighborhood, that.neighborhood) && Objects.equals(city, that.city) && Objects.equals(state, that.state) && Objects.equals(zipCode, that.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, street, complement, neighborhood, city, state, zipCode);
    }

    @Override
    public String toString() {
        return "AddressEmbeddable{" +
                "number='" + number + '\'' +
                ", street='" + street + '\'' +
                ", complement='" + complement + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
