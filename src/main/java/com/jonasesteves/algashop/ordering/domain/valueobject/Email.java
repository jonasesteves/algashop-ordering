package com.jonasesteves.algashop.ordering.domain.valueobject;

import com.jonasesteves.algashop.ordering.domain.validator.FieldValidations;

import java.util.Objects;

import static com.jonasesteves.algashop.ordering.domain.exception.ErrorMessages.VALIDATION_ERROR_EMAIL_IS_INVALID;

public record Email(String value) {

    public Email {
        FieldValidations.requiresValidEmail(value, VALIDATION_ERROR_EMAIL_IS_INVALID);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
