package com.jonasesteves.algashop.ordering.domain.valueobject;

import com.jonasesteves.algashop.ordering.domain.validator.FieldValidations;

public record ProductName(String value) {

    public ProductName {
        FieldValidations.requiresNonBlank(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
