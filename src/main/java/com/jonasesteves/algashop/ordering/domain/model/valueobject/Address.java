package com.jonasesteves.algashop.ordering.domain.model.valueobject;

import com.jonasesteves.algashop.ordering.domain.model.validator.FieldValidations;
import lombok.Builder;

import java.util.Objects;

public record Address(
        String number,
        String street,
        String complement,
        String neighborhood,
        String city,
        String state,
        ZipCode zipCode
) {

    @Builder(toBuilder = true)
    public Address {
        FieldValidations.requiresNonBlank(number);
        FieldValidations.requiresNonBlank(street);
        FieldValidations.requiresNonBlank(neighborhood);
        FieldValidations.requiresNonBlank(city);
        FieldValidations.requiresNonBlank(state);
        Objects.requireNonNull(zipCode);
    }
}
