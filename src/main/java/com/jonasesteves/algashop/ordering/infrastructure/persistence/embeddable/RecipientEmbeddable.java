package com.jonasesteves.algashop.ordering.infrastructure.persistence.embeddable;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class RecipientEmbeddable {
    private String firstName;
    private String lastName;
    private String document;
    private String phone;

    public RecipientEmbeddable() {
    }

    public RecipientEmbeddable(String firstName, String lastName, String document, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.document = document;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecipientEmbeddable that = (RecipientEmbeddable) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(document, that.document) && Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, document, phone);
    }

    @Override
    public String toString() {
        return "RecipientEmbeddable{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", document='" + document + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
